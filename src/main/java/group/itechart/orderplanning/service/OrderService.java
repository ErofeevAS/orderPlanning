package group.itechart.orderplanning.service;

import org.springframework.web.bind.annotation.RequestBody;

import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.service.dto.OrderDto;


public interface OrderService {

	Order createOrder(Order order);
}
