package group.itechart.orderplanning.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DeliveryStrategyConfig {

    @Bean
    public Map<DeliveryStrategy.DeliveryStrategyName, DeliveryStrategy> deliveryStrategies(List<DeliveryStrategy> strategies) {
        return strategies.stream()
                .collect(Collectors.toMap(DeliveryStrategy::getStrategyName, Function.identity()));
    }
}
