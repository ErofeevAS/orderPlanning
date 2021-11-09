package group.itechart.orderplanning.service.converter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.repository.entity.OrderEntry;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.service.dto.OrderEntryDto;


@Component
public class OrderConverter extends AbstractConverter<OrderDto, Order> {

	private final OrderEntryConverter orderEntryConverter;
	private final ClientRepository clientRepository;

	protected OrderConverter(final ModelMapper modelMapper, final OrderEntryConverter orderEntryConverter,
			final ClientRepository clientRepository) {
		super(modelMapper);
		this.orderEntryConverter = orderEntryConverter;
		this.clientRepository = clientRepository;
	}

	@Override
	public OrderDto toDto(final Order entity) {
		final OrderDto dto = super.toDto(entity);

		final BigDecimal totalPrice = entity.getOrderEntries().stream().map(this::calculateEntryPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		dto.setTotalPrice(totalPrice);
		final List<OrderEntryDto> orderEntryDtos =
				orderEntryConverter.toDtos(entity.getOrderEntries());
		dto.setOrderEntries(orderEntryDtos);
		return dto;
	}

	@Override
	public Order toEntity(final OrderDto dto) {

		final Order order = super.toEntity(dto);
		final Client client = getClient(dto);
		order.setClient(client);
		final List<OrderEntry> orderEntries = orderEntryConverter.toEntities(dto.getOrderEntries());


		order.setOrderEntries(orderEntries);

		return order;
	}



	private BigDecimal calculateEntryPrice(OrderEntry entry) {
		return entry.getProduct().getPrice().multiply(BigDecimal.valueOf(entry.getAmount()));
	}


	private Client getClient(final OrderDto orderDto) {
		final Long clientId = orderDto.getClientId();
		final Optional<Client> optionalClient = clientRepository.findById(clientId);
		return optionalClient.orElseThrow(() -> new EntityNotFoundException("client not found, id: " + clientId));
	}
}
