package com.itechart.group.warehouse.service.impl;

import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.itechart.group.warehouse.repository.WareHouseRepository;
import com.itechart.group.warehouse.repository.document.WareHouseDocument;
import com.itechart.group.warehouse.service.WareHouseService;


@Service
public class WareHouseServiceImpl implements WareHouseService {

	private final WareHouseRepository repository;

	private final MongoTemplate mongoTemplate;

	public WareHouseServiceImpl(WareHouseRepository repository, final MongoTemplate mongoTemplate) {
		this.repository = repository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<WareHouseDocument> findWareHousesInRadius(double lat, double lon, double radius) {
		Point point = new Point(lat, lon);
		Distance distance = new Distance(radius, Metrics.KILOMETERS);
		Circle circle = new Circle(point, distance);
		Criteria geoCriteria = Criteria.where("geometry").withinSphere(circle);
		Query query = Query.query(geoCriteria);

		return mongoTemplate.find(query, WareHouseDocument.class);
	}

	@Override
	public List<WareHouseDocument> findAll() {
		return repository.findAll();
	}

}
