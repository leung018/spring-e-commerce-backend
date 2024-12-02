package com.leungcheng.spring_simple_backend.domain.order;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class PurchaseItems {
  private final Map<String, Integer> productIdToQuantity = new java.util.HashMap<>();

  public void setPurchaseItem(String productId, int quantity) {
    if (quantity < 1) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    productIdToQuantity.put(productId, quantity);
  }

  public ImmutableMap<String, Integer> getProductIdToQuantity() {
    return ImmutableMap.copyOf(productIdToQuantity);
  }
}
