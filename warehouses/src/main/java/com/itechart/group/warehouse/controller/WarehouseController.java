package com.itechart.group.warehouse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itechart.group.warehouse.repository.document.WareHouseDocument;
import com.itechart.group.warehouse.service.WareHouseService;


@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

	private final WareHouseService wareHouseService;

	public WarehouseController(final WareHouseService wareHouseService) {
		this.wareHouseService = wareHouseService;
	}

	@GetMapping
	public List<WareHouseDocument> getWareHouses(@RequestParam double lat, @RequestParam double lon,
			@RequestParam double radius) {
		return wareHouseService.findWareHousesInRadius(lat, lon, radius);
	}

}
