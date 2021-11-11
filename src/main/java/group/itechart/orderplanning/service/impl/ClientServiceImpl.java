package group.itechart.orderplanning.service.impl;

import java.util.List;
import java.util.Optional;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group.itechart.orderplanning.repository.CityRepository;
import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.entity.City;
import group.itechart.orderplanning.repository.entity.Client;
import group.itechart.orderplanning.service.ClientService;
import group.itechart.orderplanning.service.converter.impl.ClientConverter;
import group.itechart.orderplanning.service.dto.ClientDto;


@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

	private final ClientConverter clientConverter;
	private final ClientRepository clientRepository;
	private final CityRepository cityRepository;

	public ClientServiceImpl(final ClientConverter clientConverter, final ClientRepository clientRepository,
			final CityRepository cityRepository) {
		this.clientConverter = clientConverter;
		this.clientRepository = clientRepository;
		this.cityRepository = cityRepository;
	}

	@Override
	@Transactional
	public ClientDto create(final ClientDto clientDto) {
		Client client = clientConverter.toEntity(clientDto);
		final String cityName = client.getCity().getName();
		final Optional<City> cityOptional = cityRepository.findByName(cityName);

		cityOptional.ifPresent(client::setCity);
		Client savedClient = clientRepository.save(client);
		return clientConverter.toDto(savedClient);
	}

	@Override
	public List<ClientDto> getClients() {
		final List<Client> clients = clientRepository.findAll();
		return clientConverter.toDtos(clients);
	}
}
