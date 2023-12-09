package com.store.management.service;


import com.store.management.TestConstants;
import com.store.management.model.ProductNotFoundException;
import com.store.management.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(TestConstants.MOCK_PRODUCTS));

        StepVerifier.create(productService.getAllProducts())
                .expectNextSequence(TestConstants.MOCK_PRODUCTS)
                .verifyComplete();
    }

    @Test
    void testGetProductById() {
        Long productId = TestConstants.PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.just(TestConstants.MOCK_PRODUCT_1));

        StepVerifier.create(productService.getProductById(productId))
                .expectNextMatches(product -> product.getId().equals(productId))
                .verifyComplete();
    }

    @Test
    void testGetProductById_ProductNotFound() {
        Long productId = TestConstants.PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(productId))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void testUpdateProduct() {
        Long productId = TestConstants.PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.just(TestConstants.MOCK_PRODUCT_1));
        when(productRepository.save(TestConstants.MOCK_PRODUCT_1)).thenReturn(Mono.just(TestConstants.UPDATED_PRODUCT));

        StepVerifier.create(productService.updateProduct(productId, TestConstants.UPDATED_PRODUCT))
                .expectNextMatches(product -> product.getName().equals("UpdatedProduct"))
                .verifyComplete();
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Long productId = TestConstants.PRODUCT_ID_1;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct(productId, TestConstants.UPDATED_PRODUCT))
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}

