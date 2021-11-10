package group.itechart.orderplanning.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import group.itechart.orderplanning.repository.WareHouseRepository;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.Product;
import group.itechart.orderplanning.repository.entity.StockLevel;
import group.itechart.orderplanning.repository.entity.WareHouse;


@ExtendWith(SpringExtension.class)
class WareHouseServiceTest {

	private final static String TEST_WH1_NAME = "test_wareHouse_1";
	private final static String TEST_WH2_NAME = "test_wareHouse_2";

	private final static double TEST_LATITUDE= 1;
	private final static double TEST_LONGITUDE = 2;

	private final static Map<Double, Integer> radiusToAccuracy = new LinkedHashMap<>();

	static {
		radiusToAccuracy.put(0.5, 6);
		radiusToAccuracy.put(1.0, 5);
		radiusToAccuracy.put(6.0, 4);
		radiusToAccuracy.put(20.0, 3);
		radiusToAccuracy.put(80.0, 2);
		radiusToAccuracy.put(600.0, 1);
	}

	@InjectMocks
	@Spy
	private WareHouseServiceImpl wareHouseService;

	@Mock
	private Coordinates coordinates;
	@Mock
	private WareHouseRepository wareHouseRepository;
	@Mock
	private Product product1;
	@Mock
	private Product product2;
	@Mock
	private StockLevel stockLevel1;
	@Mock
	private StockLevel stockLevel2;
	@Mock
	private WareHouse wareHouse1;
	@Mock
	private WareHouse wareHouse2;

	@Spy
	private List<WareHouse> testWarehouses;

	@BeforeEach
	public void init(){
		doReturn(TEST_LATITUDE).when(coordinates).getLatitude();
		doReturn(TEST_LONGITUDE).when(coordinates).getLongitude();

		doReturn(1L).when(product1).getId();
		doReturn("test1").when(product1).getName();
		doReturn(BigDecimal.valueOf(100)).when(product1).getPrice();

		doReturn(2L).when(product2).getId();
		doReturn("test2").when(product2).getName();
		doReturn(BigDecimal.valueOf(10)).when(product2).getPrice();


		doReturn(1L).when(stockLevel1).getId();
		doReturn(product1).when(stockLevel1).getProduct();
		doReturn(5).when(stockLevel1).getAmount();

		doReturn(2L).when(stockLevel2).getId();
		doReturn(product2).when(stockLevel2).getProduct();
		doReturn(5).when(stockLevel2).getAmount();


		doReturn(1L).when(wareHouse1).getId();
		doReturn(TEST_WH1_NAME).when(wareHouse1).getName();
		doReturn(List.of(stockLevel1)).when(wareHouse1).getStockLevels();

		doReturn(2L).when(wareHouse2).getId();
		doReturn(TEST_WH2_NAME).when(wareHouse2).getName();
		doReturn(List.of(stockLevel2)).when(wareHouse2).getStockLevels();

		testWarehouses = List.of(wareHouse1, wareHouse2);

	}

	@Test
	public void shouldFindWarehousesNearbyCoordinatesInRadius() {
		final long productId = 1L;
		final int productAmount = 5;
		doReturn(testWarehouses).when(wareHouseService).findWareHousesInRadius(coordinates, 6, 4);
		doReturn(testWarehouses).when(wareHouseService)
				.filterWareHousesByProductAvailability(productId, productAmount, testWarehouses);

		final List<WareHouse> wareHousesInCoordinates = wareHouseService.findWareHousesInCoordinates(coordinates, productId,
				productAmount);

		assertEquals(wareHousesInCoordinates, testWarehouses);
		verify(wareHouseService, times(3)).findWareHousesInRadius(any(Coordinates.class), anyDouble(), anyInt());
	}

	@Test
	public void shouldThrowEntityNotFoundExceptionWhenThereIsNoWareHouseWithProduct(){
		assertThrows(EntityNotFoundException.class,()->{
			wareHouseService.findWareHousesInCoordinates(coordinates, 1L,
					5);
		});
		verify(wareHouseService, times(5)).findWareHousesInRadius(any(Coordinates.class), anyDouble(), anyInt());
	}

	@ParameterizedTest
	@MethodSource("providedWarehouses")
	public void shouldFilterWareHousesWhenNotEnoughAmount(long id, int amount, int foundWarehouses) {
		final List<WareHouse> wareHouses = wareHouseService.filterWareHousesByProductAvailability(id, amount, testWarehouses);

		assertEquals(wareHouses.size(), foundWarehouses);
	}

	private static Stream<Arguments> providedWarehouses() {
		return Stream.of(Arguments.of(1, 5, 1), Arguments.of(2, 5, 1), Arguments.of(2, 500, 0));
	}



//	@Test
//	public void shouldFindWareHousesInRadius(){
//
////		double clientLat = 53.90110604502406;
//		double clientLat = 53.901106;
////		double clientLong = 27.555032586504367 ;
//		double clientLong = 27.55503 ;
//		String getClient = "u9edeh";
//
//
//		double lat1 = 53.887388126150405;
//		double lon1= 27.551395208902573;
//		final double distance1 = GeoHashUtils.distance(clientLat, clientLong, lat1, lon1);
//		String geoWH1 = "u9eddc";
//
//
//		double lat2 =53.849640626463795;
//		double lon2= 27.556487537545085;
//		final double distance2= GeoHashUtils.distance(clientLat, clientLong, lat2, lon2);
//		String geoWH2 = "u9ed75";
//
//		double lat3 =53.78650914283024 ;
//		double lon3= 27.58849646044086;
//		final double distance3 = GeoHashUtils.distance(clientLat, clientLong, lat3, lon3);
//		String geoWH3 = "u9e9gz";
//
//
//		double lat4 =53.59590509952606 ;
//		double lon4= 27.584211326185816;
//		final double distance4 = GeoHashUtils.distance(clientLat, clientLong, lat4, lon4);
//
//		String geoWH4 = "u9e8gs";
//
//		final List<String> commonGeoHashForCircle = getCommonGeoHashForCircle(clientLat, clientLong, 6, 4);
//
//
//	}


}