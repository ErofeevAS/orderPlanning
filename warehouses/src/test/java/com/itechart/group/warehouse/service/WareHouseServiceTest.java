package com.itechart.group.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.itechart.group.warehouse.repository.WareHouseRepository;
import com.itechart.group.warehouse.repository.document.WareHouseDocument;
import com.itechart.group.warehouse.service.impl.WareHouseServiceImpl;


@Testcontainers
@DataMongoTest(excludeAutoConfiguration = { EmbeddedMongoAutoConfiguration.class })
public class WareHouseServiceTest {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@TestConfiguration
	static class Config {
		@Bean
		WareHouseService wareHouseMongoService(@Autowired WareHouseRepository repository,
				@Autowired MongoTemplate mongoTemplate) {
			return new WareHouseServiceImpl(repository, mongoTemplate);
		}
	}

	@Autowired
	private WareHouseRepository repository;

	@Autowired
	private WareHouseService wareHouseMongoService;

	@AfterEach
	void cleanUp() {
		this.repository.deleteAll();
	}

	@ParameterizedTest
	@MethodSource("providedRadiuses")
	void shouldReturnWareHousesInRadius(double radius, double amount, List<String> names) {

		// test_point lat = 53.906319311356036 long = 26.728492459416124 closed to 1st WH
		double lat = 53.906319311356036;
		double lon = 26.728492459416124;

		this.repository.save(new WareHouseDocument(null, "test1", new GeoJsonPoint(53.90734927644237, 26.716030191307375)));
		this.repository.save(new WareHouseDocument(null, "test2", new GeoJsonPoint(53.88139327767924, 26.73376971980169)));
		this.repository.save(new WareHouseDocument(null, "test3", new GeoJsonPoint(53.87921769422419, 26.73376971980169)));

		List<WareHouseDocument> wareHouses = wareHouseMongoService.findWareHousesInRadius(lat, lon, radius);

		assertEquals(amount, wareHouses.size());
		final List<String> warehousesNames = wareHouses.stream().map(WareHouseDocument::getName).collect(Collectors.toList());
		Assertions.assertIterableEquals(names, warehousesNames);
	}

	private static Stream<Arguments> providedRadiuses() {
		List<String> names1 = Collections.emptyList();
		List<String> names2 = List.of("test1");
		List<String> names3 = List.of("test1", "test2", "test3");
		return Stream.of(Arguments.of(1, 0, names1), Arguments.of(2, 1, names2), Arguments.of(10, 3, names3));
	}

}
