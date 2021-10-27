package group.itechart.orderplanning.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.itechart.orderplanning.service.ClientService;
import group.itechart.orderplanning.service.dto.ClientDto;


@RestController
@RequestMapping("/api/v1/clients")
@Validated
public class ClientController {

	private final ClientService clientService;

	public ClientController(final ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public List<ClientDto> getClients() {
		return clientService.getClients();
	}

	@PostMapping
	public ClientDto createClient(@Valid @RequestBody ClientDto client) {

		return clientService.create(client);
	}

}
