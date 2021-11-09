package group.itechart.orderplanning.service.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
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

	private final OrderRepository orderRepository;
	private final ClientRepository clientRepository;
	private final WareHouseService wareHouseService;
	private final ProductRepository productRepository;
	private final OrderConverter orderConverter;

	public OrderServiceImpl(final OrderRepository orderRepository, final ClientRepository clientRepository, final WareHouseService wareHouseService,
			final ProductRepository productRepository, final OrderConverter orderConverter) {
		this.orderRepository = orderRepository;
		this.clientRepository = clientRepository;
		this.wareHouseService = wareHouseService;
		this.productRepository = productRepository;
		this.orderConverter = orderConverter;
	}

	@Override
	@Transactional
	public Order createOrder(final OrderDto orderDto) {
		Order order = orderConverter.toEntity(orderDto);
		final List<OrderEntry> orderEntries = populateOrderEntries(client, orderDto.getOrderEntries());

		return orderRepository.save(order);
	}

//	private Client getClient(final OrderDto orderDto) {
//		final Long clientId = orderDto.getClientId();
//		final Optional<Client> optionalClient = clientRepository.findById(clientId);
//		return optionalClient.orElseThrow(() -> new EntityNotFoundException("client not found, id: " + clientId));
//	}
//
//	private List<OrderEntry> populateOrderEntries(final Client client, List<OrderEntryDto> orderEntriesDto) {
//		if (orderEntriesDto.isEmpty() || isNull(client)) {
//			return Collections.emptyList();
//		}
//		final List<OrderEntry> orderEntries = new ArrayList<>();
//
//		final Coordinates clientCoordinates = client.getCity().getCoordinates();
//		final String clientGeoHash = GeoHash.geoHashStringWithCharacterPrecision(clientCoordinates.getLatitude(),
//				clientCoordinates.getLongitude(), GEO_HASH_ACCURACY);
//
//		for (OrderEntryDto orderEntryDto : orderEntriesDto) {
//			final Long productId = orderEntryDto.getProduct().getId();
//			final Product product = productRepository.findById(productId)
//					.orElseThrow(() -> new EntityNotFoundException("product not found, id" + productId));
//			final int productAmount = orderEntryDto.getAmount();
//			final List<WareHouse>  wareHouses = getWareHousesInCoordinates(clientCoordinates, productId, productAmount);
//			final WareHouse nearestWareHouse = findNearestWareHouse(wareHouses, clientGeoHash);
//
//			if (nonNull(nearestWareHouse)) {
//				final double distance = calculateDistance(nearestWareHouse.getGeoHash(), clientGeoHash);
//				OrderEntry orderEntry = createOrderEntry(product, productAmount, nearestWareHouse, distance);
//				orderEntries.add(orderEntry);
//			}
//		}
//		return orderEntries;
//	}







}
