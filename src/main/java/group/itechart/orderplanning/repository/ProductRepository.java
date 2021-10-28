package group.itechart.orderplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import group.itechart.orderplanning.repository.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
