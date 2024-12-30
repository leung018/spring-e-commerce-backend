package com.leungcheng.spring_simple_backend.domain.order;

import com.google.common.collect.ImmutableMap;
import com.leungcheng.spring_simple_backend.domain.Product;
import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.User;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import com.leungcheng.spring_simple_backend.validation.MyIllegalArgumentException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
  private @Autowired UserRepository userRepository;
  private @Autowired ProductRepository productRepository;
  private @Autowired OrderRepository orderRepository;

  public static class CreateOrderException extends MyIllegalArgumentException {
    // Add this static method to reduce duplication because one test in api level is interested in
    // this message. But it may not be necessary to move other error messages to this class until we
    // need them.
    public static String insufficientStockMsg(UUID productId) {
      return "Insufficient stock for product: " + productId;
    }

    public CreateOrderException(String message) {
      super(message);
    }
  }

  @Retryable(noRetryFor = CreateOrderException.class)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Order createOrder(UUID buyerUserId, PurchaseItems purchaseItems, UUID requestId) {
    if (requestId == null) {
      throw new CreateOrderException("Request ID cannot be null");
    }

    Optional<Order> order = orderRepository.findByBuyerUserIdAndRequestId(buyerUserId, requestId);
    if (order.isPresent()) {
      return order.get();
    }

    User buyer =
        getUser(buyerUserId).orElseThrow(() -> new CreateOrderException("Buyer does not exist"));

    BigDecimal totalCost = processPurchaseItems(purchaseItems);

    if (buyer.getBalance().compareTo(totalCost) < 0) {
      throw new CreateOrderException("Insufficient balance");
    }
    saveNewBalance(buyer, buyer.getBalance().subtract(totalCost));

    return addNewOrder(buyerUserId, purchaseItems, requestId);
  }

  private Optional<User> getUser(UUID userId) {
    return userRepository.findById(userId);
  }

  private void saveNewBalance(User buyer, BigDecimal newBalance) {
    User updatedBuyer = buyer.toBuilder().balance(newBalance).build();
    userRepository.save(updatedBuyer);
  }

  private Order addNewOrder(UUID buyerUserId, PurchaseItems purchaseItems, UUID requestId) {
    Order order = new Order(buyerUserId, purchaseItems, requestId);
    return orderRepository.save(order);
  }

  private BigDecimal processPurchaseItems(PurchaseItems purchaseItems) {
    ImmutableMap<UUID, Integer> productIdToQuantity = purchaseItems.getProductIdToQuantity();
    if (productIdToQuantity.isEmpty()) {
      throw new CreateOrderException("Purchase items cannot be empty");
    }

    BigDecimal totalCost = BigDecimal.ZERO;
    for (Map.Entry<UUID, Integer> entry : productIdToQuantity.entrySet()) {
      UUID productId = entry.getKey();
      int purchaseQuantity = entry.getValue();
      Product product = getProduct(productId);

      reduceProductStock(product, purchaseQuantity);
      addProfitToSeller(product, purchaseQuantity);

      BigDecimal itemCost = product.getPrice().multiply(BigDecimal.valueOf(purchaseQuantity));
      totalCost = totalCost.add(itemCost);
    }
    return totalCost;
  }

  private void reduceProductStock(Product product, int purchaseQuantity) {
    if (purchaseQuantity > product.getQuantity()) {
      throw new CreateOrderException(CreateOrderException.insufficientStockMsg(product.getId()));
    }
    int newQuantity = product.getQuantity() - purchaseQuantity;
    Product updatedProduct = product.toBuilder().quantity(newQuantity).build();
    productRepository.save(updatedProduct);
  }

  private void addProfitToSeller(Product product, int purchaseQuantity) {
    User seller = getUser(product.getUserId()).orElseThrow();
    BigDecimal profit = product.getPrice().multiply(BigDecimal.valueOf(purchaseQuantity));
    BigDecimal newBalance = seller.getBalance().add(profit);
    saveNewBalance(seller, newBalance);
  }

  private Product getProduct(UUID productId) {
    return productRepository
        .findById(productId)
        .orElseThrow(() -> new CreateOrderException("Product: " + productId + " does not exist"));
  }
}
