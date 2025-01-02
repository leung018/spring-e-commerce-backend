package com.leungcheng.spring_e_commerce_backend.domain.order;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, UUID> {
  Optional<Order> findByBuyerUserIdAndRequestId(UUID buyerUserId, UUID requestId);
}
