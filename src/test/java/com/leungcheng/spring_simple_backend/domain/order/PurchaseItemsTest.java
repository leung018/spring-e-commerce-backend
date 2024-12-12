package com.leungcheng.spring_simple_backend.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableMap;
import com.leungcheng.spring_simple_backend.validation.MyIllegalArgumentException;
import org.junit.jupiter.api.Test;

class PurchaseItemsTest {
  @Test
  void shouldSetPurchaseItemAndGetThem() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem("product_id_1", 1);
    purchaseItems.setPurchaseItem("product_id_2", 2);

    ImmutableMap<String, Integer> map = purchaseItems.getProductIdToQuantity();
    assertEquals(ImmutableMap.of("product_id_1", 1, "product_id_2", 2), map);
  }

  @Test
  void shouldDuplicateCallToSetExistingProductId_WillOverwriteTheQuantity() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem("product_id_1", 1);
    purchaseItems.setPurchaseItem("product_id_1", 2);

    ImmutableMap<String, Integer> map = purchaseItems.getProductIdToQuantity();
    assertEquals(ImmutableMap.of("product_id_1", 2), map);
  }

  @Test
  void shouldNotAllowLessThanZeroQuantity() {
    PurchaseItems purchaseItems = new PurchaseItems();

    assertThrows(
        MyIllegalArgumentException.class, () -> purchaseItems.setPurchaseItem("product_id", 0));
    assertThrows(
        MyIllegalArgumentException.class, () -> purchaseItems.setPurchaseItem("product_id", -1));

    ImmutableMap<String, Integer> map = purchaseItems.getProductIdToQuantity();
    assertEquals(0, map.size());
  }
}
