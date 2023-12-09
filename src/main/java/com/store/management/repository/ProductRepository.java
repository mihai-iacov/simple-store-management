package com.store.management.repository;

import com.store.management.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<Product, UUID> {
}
