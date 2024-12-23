package com.leungcheng.spring_simple_backend.domain.order;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"requestId", "buyerUserId"})})
public class Order {
  @Id private final UUID id = UUID.randomUUID();
  private UUID buyerUserId;
  private PurchaseItems purchaseItems;

  private UUID requestId;

  private Order() {}

  Order(UUID buyerUserId, PurchaseItems purchaseItems, UUID requestId) {
    this.buyerUserId = buyerUserId;
    this.purchaseItems = purchaseItems;
    this.requestId = requestId;
  }

  public UUID getId() {
    return id;
  }

  public UUID getBuyerUserId() {
    return buyerUserId;
  }

  public PurchaseItems getPurchaseItems() {
    return purchaseItems;
  }

  public UUID getRequestId() {
    return requestId;
  }
}
