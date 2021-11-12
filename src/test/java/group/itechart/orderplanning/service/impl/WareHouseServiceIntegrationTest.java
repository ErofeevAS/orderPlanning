package group.itechart.orderplanning.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.WareHouseService;


@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(value = { "classpath:application-test.properties" })
@ActiveProfiles({ "test" })
public class WareHouseServiceIntegrationTest extends BaseContainerTest {

	private final static Map<Double, Integer> radiusToAccuracy = new LinkedHashMap<>();

	private final static double R1 = 0.5;
	private final static double R2 = 1.0;
	private final static double R3 = 6.0;
	private final static double R4 = 20.0;
	private final static double R5 = 80.0;
	private final static double R6 = 600.0;

	private final static Long PRODUCT_ID_1 = 1L;
	private final static Long PRODUCT_ID_2 = 2L;

	static {
		radiusToAccuracy.put(R1, 6);
		radiusToAccuracy.put(R2, 5);
		radiusToAccuracy.put(R3, 4);
		radiusToAccuracy.put(R4, 3);
		radiusToAccuracy.put(R5, 2);
		radiusToAccuracy.put(R6, 1);
	}

	@Autowired
	private WareHouseService wareHouseService;

	@ParameterizedTest
	@MethodSource("testData")
	public void shouldFindNearestWareHouseInRadius(long productId, double radius, int accuracy, int amount) {
		final double lat = 53.901106;
		final double lon = 27.55503;
		final Coordinates coordinates = new Coordinates(lat, lon);

		final List<WareHouse> wareHousesInRadius = wareHouseService.findWareHousesInRadius(coordinates, radius, accuracy, productId, 1);

		assertEquals(amount, wareHousesInRadius.size());
	}

	private static Stream<Arguments> testData() {

		final List<Arguments> firstProduct = List.of(Arguments.of(PRODUCT_ID_1, R1, radiusToAccuracy.get(R1), 0),
				Arguments.of(PRODUCT_ID_1, R2, radiusToAccuracy.get(R2), 1),
				Arguments.of(PRODUCT_ID_1, R3, radiusToAccuracy.get(R3), 2),
				Arguments.of(PRODUCT_ID_1, R4, radiusToAccuracy.get(R4), 4),
				Arguments.of(PRODUCT_ID_1, R5, radiusToAccuracy.get(R5), 4),
				Arguments.of(PRODUCT_ID_1, R6, radiusToAccuracy.get(R6), 4));

		final List<Arguments> secondProduct = List.of(Arguments.of(PRODUCT_ID_2, R1, radiusToAccuracy.get(R1), 0),
				Arguments.of(PRODUCT_ID_2, R2, radiusToAccuracy.get(R2), 1),
				Arguments.of(PRODUCT_ID_2, R3, radiusToAccuracy.get(R3), 1),
				Arguments.of(PRODUCT_ID_2, R4, radiusToAccuracy.get(R4), 1),
				Arguments.of(PRODUCT_ID_2, R5, radiusToAccuracy.get(R5), 1),
				Arguments.of(PRODUCT_ID_2, R6, radiusToAccuracy.get(R6), 1));

		return Stream.concat(firstProduct.stream(), secondProduct.stream());
	}

}