package com.leungcheng.spring_e_commerce_backend.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableMap;
import com.leungcheng.spring_e_commerce_backend.validation.MyIllegalArgumentException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PurchaseItemsTest {
  UUID seedProductId1 = UUID.randomUUID();
  UUID seedProductId2 = UUID.randomUUID();

  @Test
  void shouldSetPurchaseItemAndGetThem() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem(seedProductId1, 1);
    purchaseItems.setPurchaseItem(seedProductId2, 2);

    assertEquals(
        ImmutableMap.of(seedProductId1, 1, seedProductId2, 2),
        purchaseItems.getProductIdToQuantity());
  }

  @Test
  void shouldDuplicateCallToSetExistingProductId_WillOverwriteTheQuantity() {
    PurchaseItems purchaseItems = new PurchaseItems();
    purchaseItems.setPurchaseItem(seedProductId1, 1);
    purchaseItems.setPurchaseItem(seedProductId1, 2);

    assertEquals(ImmutableMap.of(seedProductId1, 2), purchaseItems.getProductIdToQuantity());
  }

  @Test
  void shouldNotAllowLessThanZeroQuantity() {
    PurchaseItems purchaseItems = new PurchaseItems();

    assertThrows(
        MyIllegalArgumentException.class, () -> purchaseItems.setPurchaseItem(seedProductId1, 0));
    assertThrows(
        MyIllegalArgumentException.class, () -> purchaseItems.setPurchaseItem(seedProductId1, -1));

    assertEquals(0, purchaseItems.getProductIdToQuantity().size());
  }
}
