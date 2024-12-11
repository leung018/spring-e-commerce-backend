package com.leungcheng.spring_simple_backend.domain.order;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, String> {
  Optional<Order> findByRequestId(String requestId);
}
