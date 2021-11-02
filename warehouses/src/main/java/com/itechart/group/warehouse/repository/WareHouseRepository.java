package com.itechart.group.warehouse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.itechart.group.warehouse.repository.document.WareHouseDocument;


public interface WareHouseRepository extends MongoRepository<WareHouseDocument, String> {

//	List<WareHouseDocument> findWareHousesInRadius(BigDecimal lat, BigDecimal lon, double radius);

//	List<WareHouseDocument> findWareHouseDocumentByGeometryWithin()

}
