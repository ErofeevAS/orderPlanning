package group.itechart.orderplanning.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import group.itechart.orderplanning.repository.ProductRepository;
import group.itechart.orderplanning.repository.entity.Product;
import group.itechart.orderplanning.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	public ProductServiceImpl(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Optional<Product> findById(final Long productId) {
		return productRepository.findById(productId);
	}

}
