package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.auth.UserAuthenticatedInfoToken;
import com.leungcheng.spring_simple_backend.domain.order.Order;
import com.leungcheng.spring_simple_backend.domain.order.OrderService;
import com.leungcheng.spring_simple_backend.domain.order.PurchaseItems;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  @Autowired private OrderService orderService;

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  Order newOrder(
      @Valid @RequestBody CreateOrderRequest createOrderRequest,
      UserAuthenticatedInfoToken authToken) {
    PurchaseItems purchaseItems = new PurchaseItems();
    for (var entry : createOrderRequest.productIdToQuantity().entrySet()) {
      purchaseItems.setPurchaseItem(entry.getKey(), entry.getValue());
    }
    return orderService.createOrder(
        authToken.getPrincipal().userId(), purchaseItems, createOrderRequest.requestId());
  }

  public record CreateOrderRequest(
      @Size(max = 36) String requestId, Map<UUID, Integer> productIdToQuantity) {}
}
