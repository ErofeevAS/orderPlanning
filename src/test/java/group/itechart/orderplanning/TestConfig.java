package group.itechart.orderplanning;

import group.itechart.orderplanning.service.WareHouseMongoService;
import group.itechart.orderplanning.service.impl.WareHouseMongoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

//@TestConfiguration
public class TestConfig {
//    @Autowired
    private MongoTemplate mongoTemplate;

//    @Bean
    public WareHouseMongoService wareHouseMongoService() {
        return new WareHouseMongoServiceImpl(mongoTemplate);
    }
}
