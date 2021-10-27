package group.itechart.orderplanning.service.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.service.converter.Converter;
import group.itechart.orderplanning.service.dto.ClientDto;


@Component
public class ClientConverter implements Converter<ClientDto, Client> {

	private final ModelMapper modelMapper;

	public ClientConverter(final ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public ClientDto toDto(final Client entity) {
		return modelMapper.map(entity, ClientDto.class);
	}

	@Override
	public Client toEntity(final ClientDto dto) {
		return modelMapper.map(dto, Client.class);
	}

	@Override
	public List<ClientDto> toDtos(final List<Client> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}
}
