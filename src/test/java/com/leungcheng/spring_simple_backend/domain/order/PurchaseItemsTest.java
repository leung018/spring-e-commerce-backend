package com.leungcheng.spring_simple_backend.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

class PurchaseItemsTest {
  @Test
  void shouldAddPurchaseItemAndGetThem() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.addPurchaseItem("product_id_1", 1);
    purchaseItems.addPurchaseItem("product_id_2", 2);

    ImmutableMap<String, Integer> map = purchaseItems.getAll();
    assertEquals(ImmutableMap.of("product_id_1", 1, "product_id_2", 2), map);
  }
}
