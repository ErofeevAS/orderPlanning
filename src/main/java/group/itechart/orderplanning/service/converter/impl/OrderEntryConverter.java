package group.itechart.orderplanning.service.converter.impl;

import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.service.dto.OrderEntryDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class OrderEntryConverter extends AbstractConverter<OrderEntryDto, OrderEntry> {
	protected OrderEntryConverter(ModelMapper modelMapper) {
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
