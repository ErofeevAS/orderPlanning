package group.itechart.orderplanning.repository;

import java.util.List;

import group.itechart.orderplanning.repository.entity.WareHouse;


public interface CustomWareHouseRepository {

	List<WareHouse> findWareHousesByGeoHash(List<String> geoHash);

}
