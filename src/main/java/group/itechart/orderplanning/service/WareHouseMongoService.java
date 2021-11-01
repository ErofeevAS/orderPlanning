package group.itechart.orderplanning.service;

import group.itechart.orderplanning.repository.document.WareHouseDocument;

import java.math.BigDecimal;
import java.util.List;

public interface WareHouseMongoService {

    List<WareHouseDocument> findWareHousesInRadius(BigDecimal lat, BigDecimal lon, double radius);
}
