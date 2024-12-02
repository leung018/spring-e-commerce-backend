package com.leungcheng.spring_simple_backend.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

class PurchaseItemsTest {
  @Test
  void shouldSetPurchaseItemAndGetThem() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem("product_id_1", 1);
    purchaseItems.setPurchaseItem("product_id_2", 2);

    ImmutableMap<String, Integer> map = purchaseItems.getAll();
    assertEquals(ImmutableMap.of("product_id_1", 1, "product_id_2", 2), map);
  }

  @Test
  void shouldDuplicateCallToSetExistingProductId_WillOverwriteTheQuantity() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem("product_id_1", 1);
    purchaseItems.setPurchaseItem("product_id_1", 2);

    ImmutableMap<String, Integer> map = purchaseItems.getAll();
    assertEquals(ImmutableMap.of("product_id_1", 2), map);
  }
}
