package com.store.management.model;

import lombok.Builder;

@Builder
public record UpdateProductPriceDto(
        Double price
) {
    public UpdateProductDto asPartialUpdateProductDto() {
        return UpdateProductDto.builder()
                .price(price)
                .build();
    }
}
