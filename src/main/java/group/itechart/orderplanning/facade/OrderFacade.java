package group.itechart.orderplanning.facade;

import group.itechart.orderplanning.service.dto.OrderDto;


public interface OrderFacade {

	OrderDto createOrder(OrderDto orderDto);

}
