package group.itechart.orderplanning.service.impl;

import group.itechart.orderplanning.repository.OrderRepository;
import group.itechart.orderplanning.repository.entity.Order;
import group.itechart.orderplanning.service.OrderService;
import group.itechart.orderplanning.strategy.DeliveryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final Map<DeliveryStrategy.DeliveryStrategyName, DeliveryStrategy> deliveryStrategies;

    public OrderServiceImpl(final OrderRepository orderRepository, Map<DeliveryStrategy.DeliveryStrategyName, DeliveryStrategy> deliveryStrategy) {
        this.orderRepository = orderRepository;
        this.deliveryStrategies = deliveryStrategy;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        order = deliveryStrategies.get(DeliveryStrategy.DeliveryStrategyName.DEFAULT).delivery(order);
        return orderRepository.save(order);
    }

}
