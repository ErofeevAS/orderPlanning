package group.itechart.orderplanning.service.impl;

import group.itechart.orderplanning.repository.document.WareHouseDocument;
import group.itechart.orderplanning.service.WareHouseMongoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WareHouseMongoServiceImpl implements WareHouseMongoService {

    private final MongoTemplate mongoTemplate;

    public WareHouseMongoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<WareHouseDocument> findWareHousesInRadius(BigDecimal lat, BigDecimal lon, double radius) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("test"));
        List<WareHouseDocument> wareHouseDocuments = mongoTemplate.find(query, WareHouseDocument.class);
        return wareHouseDocuments;
    }
}
