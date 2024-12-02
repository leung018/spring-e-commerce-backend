package com.leungcheng.spring_simple_backend.domain.order;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class PurchaseItems {
  private Map<String, Integer> productIdsToQuantities = new java.util.HashMap<>();

  public void setPurchaseItem(String productId, int quantity) {
    productIdsToQuantities.put(productId, quantity);
  }

  public ImmutableMap<String, Integer> getAll() {
    return ImmutableMap.copyOf(productIdsToQuantities);
  }
}
