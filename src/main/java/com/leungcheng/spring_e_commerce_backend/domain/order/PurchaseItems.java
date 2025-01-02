package com.leungcheng.spring_e_commerce_backend.domain.order;

import com.google.common.collect.ImmutableMap;
import com.leungcheng.spring_e_commerce_backend.validation.MyIllegalArgumentException;
import jakarta.persistence.*;
import java.util.Map;
import java.util.UUID;

@Embeddable
public class PurchaseItems {
  public static String INVALID_QUANTITY_MSG = "Quantity must be greater than 0";

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "purchase_items",
      joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
  @MapKeyColumn(name = "product_id")
  @Column(name = "quantity")
  private final Map<UUID, Integer> productIdToQuantity = new java.util.HashMap<>();

  public void setPurchaseItem(UUID productId, int quantity) {
    if (quantity < 1) {
      throw new MyIllegalArgumentException(INVALID_QUANTITY_MSG);
    }
    productIdToQuantity.put(productId, quantity);
  }

  public ImmutableMap<UUID, Integer> getProductIdToQuantity() {
    return ImmutableMap.copyOf(productIdToQuantity);
  }
}
