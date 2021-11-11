package group.itechart.orderplanning.controller;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.itechart.orderplanning.facade.OrderFacade;
import group.itechart.orderplanning.service.OrderService;
import group.itechart.orderplanning.service.dto.OrderDto;


@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

	private final OrderFacade orderFacade;

	public OrderController(final OrderFacade orderFacade) {
		this.orderFacade = orderFacade;
	}

	@PostMapping
	public OrderDto createOrder(@Valid @RequestBody OrderDto orderDto) {
		return orderFacade.createOrder(orderDto);
	}

}
