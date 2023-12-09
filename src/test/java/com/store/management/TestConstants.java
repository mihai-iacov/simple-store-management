package com.store.management;

import com.store.management.model.CreateProductDto;
import com.store.management.model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class TestConstants {
    public static final Long PRODUCT_ID_1 = 1L;
    public static final Long PRODUCT_ID_2 = 2L;
    public static final Long PRODUCT_ID_3 = 3L;

    public static final Product MOCK_PRODUCT_1 = Product.builder()
            .id(PRODUCT_ID_1)
            .name("MockProduct1")
            .description("Description1")
            .price(15.0)
            .stock(50)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static final Product MOCK_PRODUCT_2 = Product.builder()
            .id(PRODUCT_ID_2)
            .name("MockProduct2")
            .description("Description2")
            .price(20.0)
            .stock(75)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static final Product MOCK_PRODUCT_3 = Product.builder()
            .id(PRODUCT_ID_3)
            .name("MockProduct3")
            .description("Description3")
            .price(10.0)
            .stock(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static final CreateProductDto CREATE_PRODUCT_DTO = CreateProductDto.builder()
            .name("CreateProductDto")
            .description("CreateDescription")
            .price(25.0)
            .stock(100)
            .build();
    public static final Product UPDATED_PRODUCT = Product.builder()
            .id(PRODUCT_ID_1)
            .name("UpdatedProduct")
            .description("UpdatedDescription")
            .price(30.0)
            .stock(75)
            .build();

    public static final List<Product> MOCK_PRODUCTS = List.of(MOCK_PRODUCT_1, MOCK_PRODUCT_2, MOCK_PRODUCT_3);

    private TestConstants() {
    }
}

