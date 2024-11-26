package com.leungcheng.spring_simple_backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {
  public static class Builder {
    private String name;
    private double price;
    private int quantity;
    private String userId;

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

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Product build() {
      Product product = new Product();
      product.name = name;
      product.price = price;
      product.quantity = quantity;
      product.userId = userId;
      ObjectValidator.validate(product);

      return product;
    }
  }

  private Product() {}

  @Id
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String id = java.util.UUID.randomUUID().toString();

  private String userId;

  @NotBlank private String name;

  @Min(0)
  private double price;

  @Min(0)
  private int quantity;

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
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
