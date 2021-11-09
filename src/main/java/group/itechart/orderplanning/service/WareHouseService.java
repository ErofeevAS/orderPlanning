package group.itechart.orderplanning.service;

import java.util.List;

import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.WareHouse;


public interface WareHouseService {

	List<WareHouse> findByProductId(final Long productId);

	List<WareHouse> findWareHousesInRadius(Coordinates clientCoordinates, double radius, int accuracy);

	List<WareHouse> getWareHousesInCoordinates(final Coordinates clientCoordinates, final Long productId,
			final int productAmount);

}
