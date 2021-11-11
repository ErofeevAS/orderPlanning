package group.itechart.orderplanning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import group.itechart.orderplanning.strategy.DeliveryStrategy;


@Configuration
public class DeliveryStrategyConfig {

    @Bean
    public Map<DeliveryStrategy.DeliveryStrategyName, DeliveryStrategy> deliveryStrategies(List<DeliveryStrategy> strategies) {
        return strategies.stream()
                .collect(Collectors.toMap(DeliveryStrategy::getStrategyName, Function.identity()));
    }
}
