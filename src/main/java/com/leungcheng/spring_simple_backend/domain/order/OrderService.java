package com.leungcheng.spring_simple_backend.domain.order;

import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public OrderService(
      UserRepository userRepository,
      ProductRepository productRepository,
      OrderRepository orderRepository) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
  }

  public Order createOrder(String userId, PurchaseItems purchaseItems) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
