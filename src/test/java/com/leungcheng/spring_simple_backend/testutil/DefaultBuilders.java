package com.leungcheng.spring_simple_backend.testutil;

import com.leungcheng.spring_simple_backend.domain.Product;
import com.leungcheng.spring_simple_backend.domain.User;
import java.math.BigDecimal;

public class DefaultBuilders {
  public static User.Builder userBuilder() {
    return new User.Builder()
        .username("default_user")
        .password("default_password")
        .balance(new BigDecimal("1.0"));
  }

  public static Product.Builder productBuilder() {
    return new Product.Builder()
        .name("Default Product")
        .price(new BigDecimal("0.1"))
        .userId(java.util.UUID.randomUUID())
        .quantity(1);
  }
}
