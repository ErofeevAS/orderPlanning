package group.itechart.orderplanning;

import group.itechart.orderplanning.repository.document.WareHouseDocument;
import group.itechart.orderplanning.service.WareHouseMongoService;
import group.itechart.orderplanning.service.impl.WareHouseMongoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@RunWith(SpringRunner.class)
@Testcontainers
@DataMongoTest(excludeAutoConfiguration = {EmbeddedMongoAutoConfiguration.class, DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ActiveProfiles("test")
//@SpringBootTest(classes = TestConfig.class)
@ContextConfiguration(classes = TestConfig.class)
public class WareHouseRepositoryTest {

    @TestConfiguration
    public static class TestConfig {
        @Autowired
        private MongoTemplate mongoTemplate;

        @Bean
        public WareHouseMongoService wareHouseMongoService() {
            return new WareHouseMongoServiceImpl(mongoTemplate);
        }
    }


    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WareHouseMongoService wareHouseMongoService;

    @AfterEach
    void cleanUp() {
        this.mongoTemplate.remove(WareHouseDocument.class);
    }

    @Test
    void shouldReturnListOfCustomerWithMatchingRate() {

        // test_point lat = 53.906319311356036 long = 26.728492459416124 closed to 1st WH
        BigDecimal lat = BigDecimal.valueOf(53.906319311356036);
        BigDecimal lon = BigDecimal.valueOf(26.728492459416124);

        this.mongoTemplate.save(new WareHouseDocument(null, "test1", BigDecimal.valueOf(53.90734927644237), BigDecimal.valueOf(26.716030191307375)));
        this.mongoTemplate.save(new WareHouseDocument(null, "test2", BigDecimal.valueOf(53.88139327767924), BigDecimal.valueOf(26.73376971980169)));
        this.mongoTemplate.save(new WareHouseDocument(null, "test3", BigDecimal.valueOf(53.87921769422419), BigDecimal.valueOf(26.73376971980169)));

        List<WareHouseDocument> wareHouses = wareHouseMongoService.findWareHousesInRadius(lat, lon, 100);

        assertEquals(1, wareHouses.size());
        assertEquals("test1", wareHouses.get(0).getName());
    }

}
