package com.leungcheng.spring_simple_backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {
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
            Product product = new Product();
            product.name = name;
            product.price = price;
            product.quantity = quantity;
            ObjectValidator.validate(product);

            return product;
        }
    }

    protected Product() {}

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id = java.util.UUID.randomUUID().toString();

    @NotBlank
    private String name;

    @Min(0)
    private double price;

    @Min(0)
    private int quantity;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
