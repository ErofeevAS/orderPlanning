package group.itechart.orderplanning.service.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static group.itechart.orderplanning.utils.GeoHashUtils.getCommonGeoHashForCircle;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hsr.geohash.GeoHash;
import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.OrderRepository;
import group.itechart.orderplanning.repository.ProductRepository;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.repository.entity.Product;
import group.itechart.orderplanning.repository.entity.StockLevel;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.OrderService;
import group.itechart.orderplanning.service.WareHouseService;
import group.itechart.orderplanning.service.converter.impl.OrderConverter;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.service.dto.OrderEntryDto;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	private final static int MAX_RADIUS = 1000;
	private final static int GEO_HASH_ACCURACY = 12;
	private final static double INIT_RADIUS = 0.1;
	private final static double RADIUS_MULTIPLICATION = 1.4;

	private final OrderRepository orderRepository;
	private final OrderConverter orderConverter;
	private final ClientRepository clientRepository;
	private final WareHouseService wareHouseService;
	private final ProductRepository productRepository;

	public OrderServiceImpl(final OrderRepository orderRepository, final OrderConverter orderConverter,
			final ClientRepository clientRepository, final WareHouseService wareHouseService,
			final ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.orderConverter = orderConverter;
		this.clientRepository = clientRepository;
		this.wareHouseService = wareHouseService;
		this.productRepository = productRepository;
	}

	@Override
	@Transactional
	public OrderDto createOrder(final OrderDto orderDto) {

		final Long clientId = orderDto.getClientId();

		final Optional<Client> optionalClient = clientRepository.findById(clientId);

		final Client client = optionalClient.orElseThrow(() -> new RuntimeException("client not found, id: " + clientId));

		final List<OrderEntryDto> orderEntriesDto = orderDto.getOrderEntries();

		List<OrderEntry> orderEntries = populateOrderEntries(client, emptyIfNull(orderEntriesDto));
		Order order = new Order();
		order.setOrderEntries(orderEntries);
		order.setClient(client);


		final Order savedOrder = orderRepository.save(order);

		return orderConverter.toDto(savedOrder);
	}

	private List<OrderEntry> populateOrderEntries(final Client client, List<OrderEntryDto> orderEntriesDto) {
		if (orderEntriesDto.isEmpty() || isNull(client)) {
			return Collections.emptyList();
		}

		List<OrderEntry> orderEntries = new ArrayList<>();

		final Coordinates clientCoordinates = client.getCity().getCoordinates();
		final String clientGeoHash = GeoHash.geoHashStringWithCharacterPrecision(clientCoordinates.getLatitude(),
				clientCoordinates.getLongitude(), GEO_HASH_ACCURACY);

		for (OrderEntryDto orderEntryDto : orderEntriesDto) {
			final Long productId = orderEntryDto.getProduct().getId();
			final Product product = productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("product not found, id" + productId));
			final int productAmount = orderEntryDto.getAmount();

			double radius = INIT_RADIUS;
			List<WareHouse> wareHouses = new ArrayList<>();

			while (wareHouses.isEmpty() && radius < MAX_RADIUS) {
				log.info("------SEARCHING WAREHOUSES IN {} km radius-------------", radius);

				final List<String> geoHashes = getCommonGeoHashForCircle(clientCoordinates.getLatitude(),
						clientCoordinates.getLongitude(), radius);

				wareHouses = wareHouseService.findByGeoHash(geoHashes);

				log.info("commonGeoHashesForCircle: " + geoHashes);
				log.info("------were found {} warehouses", wareHouses.size());

				wareHouses =
						wareHouses.stream()
								.map(WareHouse::getStockLevels)
								.flatMap(Collection::stream)
								.filter(sl -> sl.getProduct().getId().equals(productId))
								.filter(sl -> sl.getAmount() >= productAmount)
						.map(StockLevel::getWareHouse).collect(Collectors.toList());


				radius *= RADIUS_MULTIPLICATION;
			}

			WareHouse nearestWareHouse = findNearestWareHouse(wareHouses, clientGeoHash);

			if (nonNull(nearestWareHouse)) {
				final double distance = calculateDistance(nearestWareHouse.getGeoHash(), clientGeoHash);
				OrderEntry orderEntry = createOrderEntry(product, productAmount, nearestWareHouse, distance);
				orderEntries.add(orderEntry);
			}


		}

		return orderEntries;
	}

	private OrderEntry createOrderEntry(Product product, final int productAmount, final WareHouse nearestWareHouse,
			double distance) {
		OrderEntry orderEntry = new OrderEntry();
		orderEntry.setAmount(productAmount);
		orderEntry.setProduct(product);
		orderEntry.setWareHouse(nearestWareHouse);
		orderEntry.setDistance(distance);
		//should be another service for expansion
//		BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(productAmount));
//		orderEntry.setPrice(price);
		return orderEntry;
	}

	private WareHouse findNearestWareHouse(final List<WareHouse> wareHouses, final String clientGeoHash) {
		if (wareHouses.size() == 1) {
			return wareHouses.get(0);
		}
		double minDistance = Double.MAX_VALUE;
		WareHouse nearestWareHouse = null;

		for (WareHouse wareHouse : wareHouses) {
			final double distance = calculateDistance(wareHouse.getGeoHash(), clientGeoHash);
			if (distance < minDistance) {
				nearestWareHouse = wareHouse;
			}
		}
		return nearestWareHouse;
	}

}
