package group.itechart.orderplanning.service.converter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.service.converter.Converter;
import group.itechart.orderplanning.service.dto.OrderDto;


@Component
public class OrderConverter implements Converter<OrderDto, Order> {

	private final ModelMapper modelMapper;

	public OrderConverter(final ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public OrderDto toDto(final Order entity) {
		//should be another priceService for expansion
		final BigDecimal totalPrice = entity.getOrderEntries().stream().map(this::calculateEntryPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		final OrderDto dto = modelMapper.map(entity, OrderDto.class);
		dto.setTotalPrice(totalPrice);
		return dto;
	}

	@Override
	public Order toEntity(final OrderDto dto) {
		return modelMapper.map(dto, Order.class);
	}

	@Override
	public List<OrderDto> toDtos(final List<Order> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}

	private BigDecimal calculateEntryPrice(OrderEntry entry) {
		return entry.getProduct().getPrice().multiply(BigDecimal.valueOf(entry.getAmount()));
	}
}
