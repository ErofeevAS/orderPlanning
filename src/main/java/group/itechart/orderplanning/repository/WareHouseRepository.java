package group.itechart.orderplanning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import group.itechart.orderplanning.repository.entity.WareHouse;


public interface WareHouseRepository extends JpaRepository<WareHouse, Long>, CustomWareHouseRepository {

	@Query("SELECT wh FROM WareHouse wh join fetch  StockLevel sl on wh.id = sl.wareHouse.id WHERE sl.product.id = ?1")
	List<WareHouse> findWareHousesByProduct(Long productId);

	List<WareHouse> findWareHousesByGeoHashAfter(String geoHash);

}
