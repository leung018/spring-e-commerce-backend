package com.leungcheng.spring_simple_backend.domain.order;

import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService {
  private @Autowired UserRepository userRepository;
  private @Autowired ProductRepository productRepository;

  public Order createOrder(String userId, PurchaseItems purchaseItems) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
