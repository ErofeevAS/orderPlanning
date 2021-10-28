package group.itechart.orderplanning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import group.itechart.orderplanning.repository.entity.WareHouse;


public interface CustomWareHouseRepository {

	List<WareHouse> findWareHousesByGeoHash(List<String> geoHash);

}
