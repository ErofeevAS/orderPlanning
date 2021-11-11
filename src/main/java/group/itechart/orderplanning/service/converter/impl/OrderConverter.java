package group.itechart.orderplanning.service.converter.impl;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.service.dto.OrderEntryDto;


@Component
public class OrderConverter extends AbstractConverter<OrderDto, Order> {

	private final OrderEntryConverter orderEntryConverter;

	protected OrderConverter(final ModelMapper modelMapper, final OrderEntryConverter orderEntryConverter) {
		super(modelMapper);
		this.orderEntryConverter = orderEntryConverter;
	}

	@Override
	public OrderDto toDto(final Order entity) {
		final OrderDto dto = super.toDto(entity);

		final BigDecimal totalPrice = entity.getOrderEntries().stream().map(this::calculateEntryPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		dto.setTotalPrice(totalPrice);
		final List<OrderEntryDto> orderEntryDtos = orderEntryConverter.toDtos(entity.getOrderEntries());
		dto.setOrderEntries(orderEntryDtos);
		return dto;
	}

	@Override
	public Order toEntity(final OrderDto dto) {
		final Order order = super.toEntity(dto);
		final List<OrderEntry> orderEntries = orderEntryConverter.toEntities(dto.getOrderEntries());
		order.setOrderEntries(orderEntries);
		return order;
	}

	private BigDecimal calculateEntryPrice(OrderEntry entry) {
		return entry.getProduct().getPrice().multiply(BigDecimal.valueOf(entry.getAmount()));
	}

}
