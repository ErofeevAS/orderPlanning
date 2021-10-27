package group.itechart.orderplanning.service.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static group.itechart.orderplanning.utils.GeoHashUtils.getCommonGeoHashForCircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hsr.geohash.GeoHash;
import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.OrderRepository;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.OrderService;
import group.itechart.orderplanning.service.WareHouseService;
import group.itechart.orderplanning.service.converter.impl.OrderConverter;
import group.itechart.orderplanning.service.dto.OrderDto;


@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderConverter orderConverter;
	private final ClientRepository clientRepository;
	private final WareHouseService wareHouseService;

	public OrderServiceImpl(final OrderRepository orderRepository, final OrderConverter orderConverter,
			final ClientRepository clientRepository, final WareHouseService wareHouseService) {
		this.orderRepository = orderRepository;
		this.orderConverter = orderConverter;
		this.clientRepository = clientRepository;
		this.wareHouseService = wareHouseService;
	}

	@Override
	@Transactional
	public OrderDto createOrder(final OrderDto orderDto) {

		final Long clientId = orderDto.getClientId();

		final Optional<Client> optionalClient = clientRepository.findById(clientId);

		final Client client = optionalClient.orElseThrow(() -> new RuntimeException("client not found, id: " + clientId));

		//todo now only for first item
		final Long productId = orderDto.getOrderEntries().get(0).getProduct().getId();

		//		List<WareHouse> wareHouses = wareHouseService.findByProductId(productId);

		WareHouse nearestWarehouse = calculateNearestWareHouse(client);

		final Order order = orderConverter.toEntity(orderDto);

		final Order savedOrder = orderRepository.save(order);

		return orderConverter.toDto(savedOrder);
	}

	private WareHouse calculateNearestWareHouse(final Client client) {
		final Coordinates coordinates = client.getCity().getCoordinates();

		double radius = 0.1;
		List<WareHouse> wareHouses = new ArrayList<>();

		final String clientGeoHash = GeoHash.geoHashStringWithCharacterPrecision(coordinates.getLatitude(),
				coordinates.getLongitude(), 12);

		while (wareHouses.isEmpty() && radius < 1000) {
			System.out.println("------SEARCHING WAREHOUSES IN " + radius + " km radius-------------");

			final String commonGeoHashForCircle = getCommonGeoHashForCircle(coordinates.getLatitude(), coordinates.getLongitude(),
					radius);

			wareHouses = wareHouseService.findByGeoHash(commonGeoHashForCircle);

			System.out.println("commonGeoHashForCircle: " + commonGeoHashForCircle);
			System.out.println("------was found " + wareHouses.size() + " warehouses");
			Map<String, List<WareHouse>> actualWareHouses = new HashMap<>();
			for (WareHouse wareHouse : wareHouses) {
				final double distance = calculateDistance(wareHouse.getGeoHash(), clientGeoHash);
				System.out.println("distance " + distance);
				final String distanceString = String.valueOf(distance);

				if (actualWareHouses.containsKey(distanceString)) {
					actualWareHouses.get(distanceString).add(wareHouse);
				}
				else {
					actualWareHouses.put(distanceString, List.of(wareHouse));
				}
			}
			radius *= 6;
		}

		return null;
	}

}
