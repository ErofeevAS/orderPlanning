package group.itechart.orderplanning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class OrderPlanningApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderPlanningApplication.class, args);
    }

}
