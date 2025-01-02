package com.leungcheng.spring_e_commerce_backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leungcheng.spring_e_commerce_backend.validation.ObjectValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
  public static class Builder {
    private String name;
    private BigDecimal price;
    private int quantity;
    private UUID userId;
    private UUID id = UUID.randomUUID();

    private Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder price(BigDecimal price) {
      this.price = price;
      return this;
    }

    public Builder quantity(int quantity) {
      this.quantity = quantity;
      return this;
    }

    public Builder userId(UUID userId) {
      this.userId = userId;
      return this;
    }

    public Product build() {
      Product product = new Product();
      product.id = id;
      product.name = name;
      product.price = price;
      product.quantity = quantity;
      product.userId = userId;
      ObjectValidator.validate(product);

      return product;
    }
  }

  private Product() {}

  public Builder toBuilder() {
    return new Builder().name(name).price(price).quantity(quantity).userId(userId).id(id);
  }

  @Id
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID id;

  @NotNull private UUID userId;

  @NotBlank private String name;

  @Column(precision = BigDecimalSettings.PRECISION, scale = BigDecimalSettings.SCALE)
  @Min(0)
  private BigDecimal price;

  @Min(0)
  private int quantity;

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }
}
