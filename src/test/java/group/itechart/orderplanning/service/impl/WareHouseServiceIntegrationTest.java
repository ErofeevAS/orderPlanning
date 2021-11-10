package group.itechart.orderplanning.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

	static {
		radiusToAccuracy.put(0.5, 6);
		radiusToAccuracy.put(1.0, 5);
		radiusToAccuracy.put(6.0, 4);
		radiusToAccuracy.put(20.0, 3);
		radiusToAccuracy.put(80.0, 2);
//		radiusToAccuracy.put(600.0, 1);
	}

	@Autowired
	private WareHouseService wareHouseService;

//	@ParameterizedTest
//	@MethodSource("providedRadiuses")
	public void shouldFindNearestWareHouseInRadius(double radius, double amount, List<String> names, int accuracy) {
		final double lat = 54.076235825729185;
		final double lon = 26.783177389068793;
		final Coordinates coordinates = new Coordinates(lat, lon);

		final List<WareHouse> wareHouses = wareHouseService.findWareHousesInRadius(coordinates, radius, accuracy);

		assertEquals(amount, wareHouses.size());
		final List<String> warehousesNames = wareHouses.stream().map(WareHouse::getName).collect(Collectors.toList());
		Assertions.assertIterableEquals(names, warehousesNames);
	}

	@ParameterizedTest
	@MethodSource("testRadiuses")
	public void test(double radius, int accuracy, int amount){
		final double lat = 53.901106;
		final double lon = 27.55503;
		final Coordinates coordinates = new Coordinates(lat, lon);

		final List<WareHouse> wareHousesInRadius = wareHouseService.findWareHousesInRadius(coordinates, radius, accuracy);

		assertEquals(amount, wareHousesInRadius.size());
	}

	private static Stream<Arguments> testRadiuses(){
		double r1 = 0.5;
		double r2 = 1.0;
		double r3 = 6.0;
		double r4 = 20.0;
		double r5 = 80.0;

		return Stream.of(Arguments.of(r1, radiusToAccuracy.get(r1), 0), Arguments.of(r2, radiusToAccuracy.get(r2), 1),
				Arguments.of(r3, radiusToAccuracy.get(r3), 2), Arguments.of(r4, radiusToAccuracy.get(r4), 4),
				Arguments.of(r5, radiusToAccuracy.get(r5), 4));
	}



	private static Stream<Arguments> providedRadiuses() {
		List<String> names1 = Collections.emptyList();
		List<String> names2 = List.of("test2", "test3");
		return Stream.of(Arguments.of(1, 0, names1, 6), Arguments.of(20, 2, names2, 4));
	}

}