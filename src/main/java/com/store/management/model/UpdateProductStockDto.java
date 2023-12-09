package com.store.management.model;

import lombok.Builder;

@Builder
public record UpdateProductStockDto(
        Integer stock
) {
    public UpdateProductDto asPartialUpdateProductDto() {
        return UpdateProductDto.builder()
                .stock(stock)
                .build();
    }
}
