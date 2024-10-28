package com.leungcheng.spring_simple_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
class Product {
    static class Builder {
        private String name;
        private double price;
        private int quantity;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Product build() {
            return new Product(name, price, quantity);
        }
    }

    @Id
    public final String id;

    @NotBlank
    public final String name;

    @Min(0)
    public final double price;

    @Min(0)
    public final int quantity;

    public Product(String name, double price, int quantity) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.quantity = quantity;

        ObjectValidator.validate(this);
    }
}
