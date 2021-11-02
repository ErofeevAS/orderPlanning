package com.itechart.group.warehouse.service;

import java.util.List;

import com.itechart.group.warehouse.repository.document.WareHouseDocument;


public interface WareHouseService {

	List<WareHouseDocument> findWareHousesInRadius(double lat, double lon, double radius);
}
