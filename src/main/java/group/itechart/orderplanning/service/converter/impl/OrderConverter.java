package group.itechart.orderplanning.service.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.Order;
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
		return modelMapper.map(entity, OrderDto.class);
	}

	@Override
	public Order toEntity(final OrderDto dto) {
		return modelMapper.map(dto, Order.class);
	}

	@Override
	public List<OrderDto> toDtos(final List<Order> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}
}
