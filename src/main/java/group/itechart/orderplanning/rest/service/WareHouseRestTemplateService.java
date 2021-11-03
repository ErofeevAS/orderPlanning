package group.itechart.orderplanning.rest.service;

import java.util.List;

import group.itechart.orderplanning.rest.document.WareHouseDocument;


public interface WareHouseRestTemplateService {

	List<WareHouseDocument> find(double lat, double lon, double radius);

}
