package com.store.management.service;

import com.store.management.model.*;
import com.store.management.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public Mono<Product> getProductById(UUID id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .doOnSuccess(product -> logger.info("Retrieved product: {}.", product))
                .doOnError(error -> logger.warn("Product with ID {} not found.", id, error));
    }

    public Mono<Product> createProduct(CreateProductDto createProductDto) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(createProductDto.name())
                .description(createProductDto.description())
                .price(createProductDto.price())
                .stock(createProductDto.stock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .newEntity(true)
                .build();
        return productRepository.save(product)
                .doOnSuccess(savedProduct -> logger.info("Created product: {}.", savedProduct));
    }

    public Mono<Product> updateProduct(UUID id, UpdateProductDto updateProductDto) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(existingProduct -> {
                    updateFields(updateProductDto, existingProduct);
                    return productRepository.save(existingProduct);
                })
                .doOnSuccess(savedProduct -> logger.info("Updated product: {}.", savedProduct));
    }

    private static void updateFields(UpdateProductDto updateProductDto, Product existingProduct) {
        if (updateProductDto.name() != null) {
            existingProduct.setName(updateProductDto.name());
        }
        if (updateProductDto.description() != null) {
            existingProduct.setDescription(updateProductDto.description());
        }
        if (updateProductDto.price() != null) {
            existingProduct.setPrice(updateProductDto.price());
        }
        if (updateProductDto.stock() != null) {
            existingProduct.setStock(updateProductDto.stock());
        }
        existingProduct.setUpdatedAt(LocalDateTime.now());
    }

    public Mono<Void> deleteProduct(UUID id) {
        return productRepository.deleteById(id)
                .doOnSuccess(unused -> logger.info("Deleted product with ID: {}.", id));
    }
}
