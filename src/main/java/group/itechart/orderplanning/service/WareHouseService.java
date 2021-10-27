package group.itechart.orderplanning.service;

import java.util.List;

import group.itechart.orderplanning.repository.entity.WareHouse;


public interface WareHouseService {

	List<WareHouse> findByProductId(final Long productId);

	List<WareHouse> findByGeoHash(String geoHash);
}
