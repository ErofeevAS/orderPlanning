package group.itechart.orderplanning.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import group.itechart.orderplanning.facade.OrderFacade;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.service.dto.OrderEntryDto;
import group.itechart.orderplanning.service.dto.ProductDto;


@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(value = { "classpath:application-test.properties" })
@ActiveProfiles({"test"})
public class OrderServiceTest extends BaseContainerTest {

	@Autowired
	private OrderFacade orderFacade;

	@Test
	void shouldReturnOrderWhenClientMakeOrder() {
		final OrderDto testOrder = createTestOrder();

		final OrderDto order = orderFacade.createOrder(testOrder);

		assertEquals(2, order.getOrderEntries().size());
		assertEquals("5100.00", order.getTotalPrice().toString());
	}

	private OrderDto createTestOrder() {
		OrderDto orderDto = new OrderDto();
		orderDto.setClientId(1L);
		List<OrderEntryDto> orderEntryDtos = new ArrayList<>();

		OrderEntryDto orderEntryDto1 = new OrderEntryDto();
		orderEntryDto1.setProduct(new ProductDto(1L, "test1", BigDecimal.valueOf(5)));
		orderEntryDto1.setAmount(5);
		orderEntryDtos.add(orderEntryDto1);

		OrderEntryDto orderEntryDto2 = new OrderEntryDto();
		orderEntryDto2.setProduct(new ProductDto(3L, "test2", BigDecimal.valueOf(6)));
		orderEntryDto2.setAmount(6);
		orderEntryDtos.add(orderEntryDto2);
		orderDto.setOrderEntries(orderEntryDtos);

		return orderDto;
	}

}
