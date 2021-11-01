package group.itechart.orderplanning.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.itechart.orderplanning.service.dto.WareHouseDto;


@RestController
@RequestMapping("/api/v1/warehouses")
@Validated
public class WareHouseController {

	@GetMapping
	public List<WareHouseDto> getWareHouses() {
		throw new UnsupportedOperationException();
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('admin')")
	public WareHouseDto create(@Valid @RequestBody WareHouseDto wareHouse) {
		throw new UnsupportedOperationException();
	}
}
