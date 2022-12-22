package group.itechart.orderplanning.service;

import java.util.Optional;

import group.itechart.orderplanning.repository.entity.Product;


public interface ProductService {

	Optional<Product> findById(Long productId);
}
