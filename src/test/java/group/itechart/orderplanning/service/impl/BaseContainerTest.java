package group.itechart.orderplanning.service.impl;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;


public abstract class BaseContainerTest {

	private static final String MYSQL_VERSION = "mysql:8.0.27";

	protected static MySQLContainer<?> MYSQL_CONTAINER;

	static {
		MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_VERSION);
		MYSQL_CONTAINER.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
	}
}
