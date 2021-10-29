package group.itechart.orderplanning.service.converter.impl;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.service.dto.OrderEntryDto;


@Component
public class OrderEntryConverter extends AbstractConverter<OrderEntryDto, OrderEntry> {

	protected OrderEntryConverter(final ModelMapper modelMapper) {
		super(modelMapper);
	}

	@Override
	public OrderEntryDto toDto(final OrderEntry entity) {
		final OrderEntryDto orderEntryDto = super.toDto(entity);
		orderEntryDto.setPrice(calculatePrice(entity));
		return orderEntryDto;
	}

	private BigDecimal calculatePrice(OrderEntry entity) {
		return entity.getProduct().getPrice().multiply(BigDecimal.valueOf(entity.getAmount()));
	}

}
