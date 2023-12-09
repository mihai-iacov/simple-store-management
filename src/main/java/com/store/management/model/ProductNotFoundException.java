package com.store.management.model;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID id) {
        super("Product with ID " + id + " not found.");
    }
}
