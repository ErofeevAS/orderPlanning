package group.itechart.orderplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import group.itechart.orderplanning.repository.entity.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
