package com.store.management.controller;

import com.store.management.TestConstants;
import com.store.management.config.SecurityConfig;
import com.store.management.model.Product;
import com.store.management.model.UpdateProductPriceDto;
import com.store.management.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static com.store.management.config.SecurityRole.ADMIN;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@WebFluxTest(controllers = ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {
    public static final UUID PRODUCT_ID = UUID.randomUUID();

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetProducts_whenAuthenticated_thenOk() {
        webTestClient
                .mutateWith(mockUser())
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class);
    }

    @Test
    public void testGetProducts_whenNotAuthenticated_thenUnauthorized() {
        webTestClient
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void testGetProduct_whenAuthenticated_thenOk() {
        webTestClient
                .mutateWith(mockUser())
                .get()
                .uri("/products/{productId}", PRODUCT_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }

    @Test
    public void testGetProduct_whenNotAuthenticated_thenUnauthorized() {
        webTestClient
                .get()
                .uri("/products/{productId}", PRODUCT_ID)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void testCreateProduct_whenUserIsAdmin_thenOk() {
        webTestClient
                .mutateWith(mockUser().roles(ADMIN.name()))
                .post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TestConstants.CREATE_PRODUCT_DTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COMMERCIAL", "WAREHOUSE"})
    public void testCreateProduct_whenUserIsNotAdmin_thenForbidden(String role) {
        webTestClient
                .mutateWith(mockUser().roles("COMMERCIAL"))
                .post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TestConstants.CREATE_PRODUCT_DTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "COMMERCIAL"})
    public void testUpdatePrice_whenUserHasAccess_thenOk(String role) {
        UpdateProductPriceDto updatePriceDto = new UpdateProductPriceDto(10.0);

        webTestClient
                .mutateWith(mockUser().roles(role))
                .put()
                .uri("/products/{productId}/updatePrice", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePriceDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }

    @Test
    public void testUpdatePrice_whenUserDoesNotHaveAccess_thenForbidden() {
        UpdateProductPriceDto updatePriceDto = new UpdateProductPriceDto(10.0);

        webTestClient
                .mutateWith(mockUser().roles("WAREHOUSE"))
                .put()
                .uri("/products/{productId}/updatePrice", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePriceDto)
                .exchange()
                .expectStatus().isForbidden();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "WAREHOUSE"})
    public void testUpdateStock_whenUserHasAccess_thenOk(String role) {
        UpdateProductPriceDto updatePriceDto = new UpdateProductPriceDto(10.0);

        webTestClient
                .mutateWith(mockUser().roles(role))
                .put()
                .uri("/products/{productId}/updateStock", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePriceDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }

    @Test
    public void testUpdateStock_whenUserDoesNotHaveAccess_thenForbidden() {
        UpdateProductPriceDto updatePriceDto = new UpdateProductPriceDto(10.0);

        webTestClient
                .mutateWith(mockUser().roles("COMMERCIAL"))
                .put()
                .uri("/products/{productId}/updateStock", PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatePriceDto)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testDeleteProduct_whenUserIsAdmin_thenOk() {
        webTestClient
                .mutateWith(mockUser().roles(ADMIN.name()))
                .delete()
                .uri("/products/{productId}", PRODUCT_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COMMERCIAL", "WAREHOUSE"})
    public void testDeleteProduct_whenUserIsNotAdmin_thenForbidden(String role) {
        webTestClient
                .mutateWith(mockUser().roles(role))
                .delete()
                .uri("/products/{productId}", PRODUCT_ID)
                .exchange()
                .expectStatus().isForbidden();
    }
}
