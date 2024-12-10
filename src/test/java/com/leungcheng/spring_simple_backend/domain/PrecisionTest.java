package com.leungcheng.spring_simple_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PrecisionTest {
  private @Autowired ProductRepository productRepository;
  private @Autowired UserRepository userRepository;

  private static Product.Builder productBuilder() {
    return new Product.Builder()
        .name("Default Product")
        .price(new BigDecimal("0.1"))
        .userId("user_01")
        .quantity(1);
  }

  private static User.Builder userBuilder() {
    return new User.Builder()
        .username("default_user")
        .password("default_password")
        .balance(new BigDecimal("1.0"));
  }

  @Test
  void shouldProductKeepPrecisionAfterSavingToRepository() {
    Product product = productBuilder().price(new BigDecimal("123456.78912")).build();
    productRepository.save(product);
    Product savedProduct = productRepository.findById(product.getId()).orElseThrow();

    assertBigDecimalEquals(product.getPrice(), savedProduct.getPrice());
  }

  @Test
  void shouldUserKeepPrecisionAfterSavingToRepository() {
    User user = userBuilder().balance(new BigDecimal("123456.78912")).build();
    userRepository.save(user);
    User savedUser = userRepository.findById(user.getId()).orElseThrow();

    assertBigDecimalEquals(user.getBalance(), savedUser.getBalance());
  }

  private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
    assertEquals(0, expected.compareTo(actual));
  }
}
