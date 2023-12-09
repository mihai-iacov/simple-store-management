package com.store.management.service;

import com.store.management.model.Product;
import com.store.management.model.ProductNotFoundException;
import com.store.management.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .doOnSuccess(product -> logger.info("Retrieved product: {}.", product))
                .doOnError(error -> logger.warn("Product with ID {} not found.", id, error));
    }

    public Mono<Product> createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product)
                .doOnSuccess(savedProduct -> logger.info("Created product: {}.", savedProduct));
    }

    public Mono<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setStock(updatedProduct.getStock());
                    existingProduct.setUpdatedAt(LocalDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .doOnSuccess(savedProduct -> logger.info("Updated product: {}.", savedProduct));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.deleteById(id)
                .doOnSuccess(unused -> logger.info("Deleted product with ID: {}.", id));
    }
}
