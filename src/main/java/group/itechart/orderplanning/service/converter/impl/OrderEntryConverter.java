package group.itechart.orderplanning.service.converter.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ch.hsr.geohash.GeoHash;
import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.ProductRepository;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.repository.entity.Product;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.WareHouseService;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.service.dto.OrderEntryDto;


@Component
public class OrderEntryConverter extends AbstractConverter<OrderDto, OrderEntry> {

	private final ClientRepository clientRepository;
	private final ProductRepository productRepository;
	private final WareHouseService wareHouseService;
	private final static int GEO_HASH_ACCURACY = 6;

	protected OrderEntryConverter(final ModelMapper modelMapper, final ClientRepository clientRepository,
			final ProductRepository productRepository, final WareHouseService wareHouseService) {
		super(modelMapper);
		this.clientRepository = clientRepository;
		this.productRepository = productRepository;
		this.wareHouseService = wareHouseService;
	}

	@Override
	public OrderDto toDto(final OrderEntry entity) {
		final OrderDto orderEntryDto = super.toDto(entity);
		orderEntryDto.setPrice(calculatePrice(entity));
		return orderEntryDto;
	}

//	@Override
//	public OrderEntry toEntity(final OrderEntryDto dto) {
//		final OrderEntry orderEntry = super.toEntity(dto);
//	}

	@Override
	public List<OrderEntry> toEntities(final List<OrderDto> dtos) {

		throw new UnsupportedOperationException();
	}

	private BigDecimal calculatePrice(OrderEntry entity) {
		return entity.getProduct().getPrice().multiply(BigDecimal.valueOf(entity.getAmount()));
	}

	private List<OrderEntry> populateOrderEntries(final Client client, List<OrderEntryDto> orderEntriesDto) {
		if (orderEntriesDto.isEmpty() || isNull(client)) {
			return Collections.emptyList();
		}
		final List<OrderEntry> orderEntries = new ArrayList<>();

		final Coordinates clientCoordinates = client.getCity().getCoordinates();
		final String clientGeoHash = GeoHash.geoHashStringWithCharacterPrecision(clientCoordinates.getLatitude(),
				clientCoordinates.getLongitude(), GEO_HASH_ACCURACY);

		for (OrderEntryDto orderEntryDto : orderEntriesDto) {
			final Long productId = orderEntryDto.getProduct().getId();
			final Product product = productRepository.findById(productId)
					.orElseThrow(() -> new EntityNotFoundException("product not found, id" + productId));
			final int productAmount = orderEntryDto.getAmount();
			final List<WareHouse>  wareHouses = wareHouseService.getWareHousesInCoordinates(clientCoordinates, productId, productAmount);
			final WareHouse nearestWareHouse = findNearestWareHouse(wareHouses, clientGeoHash);

			if (nonNull(nearestWareHouse)) {
				final double distance = calculateDistance(nearestWareHouse.getGeoHash(), clientGeoHash);
				OrderEntry orderEntry = createOrderEntry(product, productAmount, nearestWareHouse, distance);
				orderEntries.add(orderEntry);
			}
		}
		return orderEntries;
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

	private OrderEntry createOrderEntry(Product product, final int productAmount, final WareHouse nearestWareHouse,
			double distance) {
		OrderEntry orderEntry = new OrderEntry();
		orderEntry.setAmount(productAmount);
		orderEntry.setProduct(product);
		orderEntry.setWareHouse(nearestWareHouse);
		orderEntry.setDistance(distance);
		return orderEntry;
	}

}
