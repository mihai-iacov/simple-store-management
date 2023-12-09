
package com.store.management.controller;

import com.store.management.model.CreateProductDto;
import com.store.management.model.Product;
import com.store.management.model.UpdateProductPriceDto;
import com.store.management.model.UpdateProductDto;
import com.store.management.service.ProductService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody CreateProductDto product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable UUID id, @RequestBody UpdateProductDto updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @PutMapping("/{productId}/updatePrice")
    public Mono<Product> updatePrice(@PathVariable UUID productId, @RequestBody UpdateProductPriceDto updateProductPriceDto) {
        return productService.updateProduct(productId, updateProductPriceDto.asPartialUpdateProductDto());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable UUID id) {
        return productService.deleteProduct(id);
    }
}
