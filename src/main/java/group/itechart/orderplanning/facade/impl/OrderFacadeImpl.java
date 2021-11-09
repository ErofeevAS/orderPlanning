package group.itechart.orderplanning.facade.impl;

import org.springframework.stereotype.Component;

import group.itechart.orderplanning.facade.OrderFacade;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.service.OrderService;
import group.itechart.orderplanning.service.converter.impl.OrderConverter;
import group.itechart.orderplanning.service.dto.OrderDto;


@Component
public class OrderFacadeImpl implements OrderFacade {


	private final OrderService orderService;
	private final OrderConverter orderConverter;

	public OrderFacadeImpl(final OrderService orderService, final OrderConverter orderConverter) {
		this.orderService = orderService;
		this.orderConverter = orderConverter;
	}

	@Override
	public OrderDto createOrder(final OrderDto orderDto) {
		final Order order = orderService.createOrder(orderDto);
		return orderConverter.toDto(order);
	}
}
