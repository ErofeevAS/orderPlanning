package group.itechart.orderplanning.strategy;

import group.itechart.orderplanning.repository.entity.Order;

public interface DeliveryStrategy {
    Order delivery(Order order);

    DeliveryStrategyName getStrategyName();

    enum DeliveryStrategyName {
        DEFAULT
    }
}
