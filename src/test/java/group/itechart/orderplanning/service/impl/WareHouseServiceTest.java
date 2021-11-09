package group.itechart.orderplanning.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
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
@ActiveProfiles({"test"})
public class WareHouseServiceTest extends BaseContainerTest {

	@Autowired
	private WareHouseService wareHouseService;

	@ParameterizedTest
	@MethodSource("providedRadiuses")
	public void shouldFindNearestWareHouseInRadius(double radius, double amount, List<String> names) {
		final double lat = 54.076235825729185;
		final double lon = 26.783177389068793;
		final Coordinates coordinates = new Coordinates(lat, lon);

		final List<WareHouse> wareHouses = wareHouseService.findWareHousesInRadius(coordinates, radius,9);

		assertEquals(amount, wareHouses.size());
		final List<String> warehousesNames = wareHouses.stream().map(WareHouse::getName).collect(Collectors.toList());
		Assertions.assertIterableEquals(names, warehousesNames);
	}

	private static Stream<Arguments> providedRadiuses() {
		List<String> names1 = Collections.emptyList();
		List<String> names2 = List.of("test1", "test2", "test3");
		return Stream.of(Arguments.of(1, 0, names1), Arguments.of(20, 3, names2));
	}

}