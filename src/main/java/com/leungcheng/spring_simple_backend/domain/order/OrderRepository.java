package com.leungcheng.spring_simple_backend.domain.order;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {
  Optional<Order> findByRequestId(String requestId);
}
