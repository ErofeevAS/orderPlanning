package group.itechart.orderplanning.strategy.impl;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.entity.City;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.repository.entity.Product;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.ClientService;
import group.itechart.orderplanning.service.ProductService;
import group.itechart.orderplanning.service.WareHouseService;


@ExtendWith(SpringExtension.class)
class DefaultDeliveryStrategyTest {

	private static final double TEST_CLIENT_LATITUDE = 57.1;
	private static final double TEST_CLIENT_LONGITUDE = 26.1;

	@InjectMocks
	private DefaultDeliveryStrategy strategy;
	@Mock
	private WareHouseService wareHouseService;
	@Mock
	private ProductService productService;
	@Mock
	private ClientRepository clientRepository;
	@Mock
	private Order order;
	@Mock
	private OrderEntry entry1;
	@Mock
	private OrderEntry entry2;
	@Mock
	private Product product1;
	@Mock
	private Product product2;
	@Mock
	private Client client;
	@Mock
	private City city;
	@Mock
	private Coordinates coordinates;
	@Mock
	private WareHouse wareHouse1;
	@Mock
	private WareHouse wareHouse2;
	@Spy
	private List<OrderEntry> orderEntries;
	@Spy
	private List<WareHouse> wareHouses;

	@BeforeEach
	public void init() {
		doReturn(TEST_CLIENT_LATITUDE).when(coordinates).getLatitude();
		doReturn(TEST_CLIENT_LONGITUDE).when(coordinates).getLongitude();
		doReturn(coordinates).when(city).getCoordinates();
		doReturn(city).when(client).getCity();
		doReturn(1L).when(client).getId();
		doReturn(client).when(order).getClient();

		doReturn(1L).when(entry1).getId();
		doReturn(product1).when(entry1).getProduct();
		doReturn(5).when(entry1).getAmount();

		doReturn(2L).when(entry2).getId();
		doReturn(product2).when(entry2).getProduct();
		doReturn(10).when(entry2).getAmount();

		doReturn(1L).when(product1).getId();
		doReturn("test1").when(product1).getName();
		doReturn(BigDecimal.valueOf(100)).when(product1).getPrice();

		doReturn(2L).when(product2).getId();
		doReturn("test2").when(product2).getName();
		doReturn(BigDecimal.valueOf(200)).when(product2).getPrice();

		orderEntries = List.of(entry1, entry2);

		doReturn(orderEntries).when(order).getOrderEntries();

		doReturn("u9e5r3ts").when(wareHouse1).getGeoHash();
		doReturn("u9e5pe7jx").when(wareHouse2).getGeoHash();

		wareHouses = List.of(wareHouse1, wareHouse2);

	}

	@Test
	public void shouldNotExecuteWhenClientIsNull() {
		doReturn(null).when(order).getClient();

		strategy.delivery(order);

		verify(city, never()).getCoordinates();
	}

	@Test
	public void shouldNotExecuteWhenOrderEntriesIsEmpty() {
		doReturn(EMPTY_LIST).when(order).getOrderEntries();

		strategy.delivery(order);

		verify(city, never()).getCoordinates();
	}

	@Test
	public void shouldThrowExceptionWhenThereIsNoProductInDataBase() {
		doReturn(Optional.empty()).when(productService).findById(1L);

		assertThrows(RuntimeException.class, () -> {
			strategy.delivery(order);
		});
	}

	@Test
	public void shouldPopulateOrderWithNearestWareHouse() {
		doReturn(Optional.of(client)).when(clientRepository).findById(1L);
		doReturn(Optional.of(product1)).when(productService).findById(1L);
		doReturn(Optional.of(product2)).when(productService).findById(2L);
		doReturn(List.of(wareHouse1)).when(wareHouseService).findWareHousesInCoordinates(eq(coordinates), eq(1L), anyInt());
		doReturn(List.of(wareHouse2)).when(wareHouseService).findWareHousesInCoordinates(eq(coordinates), eq(2L), anyInt());

		strategy.delivery(order);

		verify(entry1).setWareHouse(wareHouse1);
		verify(entry2).setWareHouse(wareHouse2);
	}

	@Test
	public void shouldReturnNearestWareHouse() {
		final Coordinates coordinates = client.getCity().getCoordinates();
		final String clientGeoHash = strategy.getClientGeoHash(coordinates);

		final WareHouse nearestWareHouse = strategy.findNearestWareHouse(wareHouses, clientGeoHash);

		assertEquals(wareHouse1, nearestWareHouse);
	}

}