package group.itechart.orderplanning.service;

import java.util.List;

import group.itechart.orderplanning.service.dto.ClientDto;


public interface ClientService {

	ClientDto create(ClientDto client);

	//todo must be pagination
	List<ClientDto> getClients();
}
