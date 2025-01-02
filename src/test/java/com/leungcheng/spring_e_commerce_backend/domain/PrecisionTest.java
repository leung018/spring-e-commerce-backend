package com.leungcheng.spring_e_commerce_backend.domain;

import static com.leungcheng.spring_e_commerce_backend.testutil.CustomAssertions.assertBigDecimalEquals;
import static com.leungcheng.spring_e_commerce_backend.testutil.DefaultBuilders.productBuilder;
import static com.leungcheng.spring_e_commerce_backend.testutil.DefaultBuilders.userBuilder;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PrecisionTest {
  private @Autowired ProductRepository productRepository;
  private @Autowired UserRepository userRepository;

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
}
