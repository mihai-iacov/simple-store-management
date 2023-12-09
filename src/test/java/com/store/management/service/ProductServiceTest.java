package com.store.management.service;


import com.store.management.model.Product;
import com.store.management.model.ProductNotFoundException;
import com.store.management.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.store.management.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(MOCK_PRODUCTS));

        StepVerifier.create(productService.getAllProducts())
                .expectNextSequence(MOCK_PRODUCTS)
                .verifyComplete();
    }

    @Test
    void testGetProductById() {
        UUID productId = PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.just(MOCK_PRODUCT_1));

        StepVerifier.create(productService.getProductById(productId))
                .expectNextMatches(product -> product.getId().equals(productId))
                .verifyComplete();
    }

    @Test
    void testGetProductById_ProductNotFound() {
        UUID productId = PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(productId))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(MOCK_PRODUCT_1));

        StepVerifier.create(productService.createProduct(CREATE_PRODUCT_DTO))
                .expectNext(MOCK_PRODUCT_1)
                .verifyComplete();
    }

    @Test
    void testUpdateProduct() {
        UUID productId = PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.just(MOCK_PRODUCT_1));
        when(productRepository.save(MOCK_PRODUCT_1)).thenReturn(Mono.just(UPDATED_PRODUCT));

        StepVerifier.create(productService.updateProduct(productId, UPDATE_PRODUCT_DTO))
                .expectNextMatches(product -> product.getName().equals("UpdatedProduct"))
                .verifyComplete();
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        UUID productId = PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct(productId, UPDATE_PRODUCT_DTO))
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}
