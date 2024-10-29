package com.leungcheng.spring_simple_backend.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static Product.Builder productBuilder() {
        return new Product.Builder()
                .name("Default Product")
                .price(2.0)
                .quantity(3);
    }

    @Test
    void shouldCreateProduct() {
        Product product = productBuilder()
                .name("Product 1")
                .price(1.0)
                .quantity(50)
                .build();

        assertEquals("Product 1", product.name);
        assertEquals(1.0, product.price);
        assertEquals(50, product.quantity);
    }

    @Test
    void shouldIdIsDifferentForEachBuild() {
        Product product1 = productBuilder().build();
        Product product2 = productBuilder().build();

        assertNotEquals(product1.id, product2.id);
    }

    @Test
    void shouldRaiseExceptionIfViolateTheValidationConstraints() {
        Class<IllegalArgumentException> expected = IllegalArgumentException.class;
        assertThrows(expected, () -> {
            productBuilder()
                    .quantity(-1)
                    .build();
        });
        assertThrows(expected, () -> {
            productBuilder()
                    .price(-1)
                    .build();
        });
        assertThrows(expected, () -> {
            productBuilder()
                    .name("")
                    .build();
        });
    }

    @Test
    void shouldNotRaiseExceptionIfZero() {
        assertDoesNotThrow(() -> {
            productBuilder()
                    .quantity(0)
                    .price(0)
                    .build();
        });
    }
}