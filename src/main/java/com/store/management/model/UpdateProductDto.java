package com.store.management.model;

import lombok.Builder;

@Builder
public record UpdateProductDto(
        String name,
        String description,
        Double price,
        Integer stock
) {
}
