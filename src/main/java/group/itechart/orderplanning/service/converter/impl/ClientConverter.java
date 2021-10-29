package group.itechart.orderplanning.service.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.service.converter.Converter;
import group.itechart.orderplanning.service.dto.ClientDto;


@Component
public class ClientConverter extends AbstractConverter<ClientDto, Client> {

	protected ClientConverter(final ModelMapper modelMapper) {
		super(modelMapper);
	}

}
