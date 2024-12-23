package com.leungcheng.spring_simple_backend.domain.order;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"requestId", "buyerUserId"})})
public class Order {
  @Id private final String id = java.util.UUID.randomUUID().toString();
  private UUID buyerUserId;
  private PurchaseItems purchaseItems;

  private String requestId;

  private Order() {}

  Order(UUID buyerUserId, PurchaseItems purchaseItems, String requestId) {
    this.buyerUserId = buyerUserId;
    this.purchaseItems = purchaseItems;
    this.requestId = requestId;
  }

  public String getId() {
    return id;
  }

  public UUID getBuyerUserId() {
    return buyerUserId;
  }

  public PurchaseItems getPurchaseItems() {
    return purchaseItems;
  }

  public String getRequestId() {
    return requestId;
  }
}
