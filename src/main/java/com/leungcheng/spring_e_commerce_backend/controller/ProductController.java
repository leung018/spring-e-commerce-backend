package com.leungcheng.spring_e_commerce_backend.controller;

import com.leungcheng.spring_e_commerce_backend.auth.UserAuthenticatedInfoToken;
import com.leungcheng.spring_e_commerce_backend.domain.Product;
import com.leungcheng.spring_e_commerce_backend.domain.ProductRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
  @Autowired private ProductRepository repository;

  @PostMapping("/products")
  @ResponseStatus(HttpStatus.CREATED)
  Product newProduct(
      @Valid @RequestBody CreateProductRequest createProductRequest,
      UserAuthenticatedInfoToken authToken) {
    Product product =
        new Product.Builder()
            .name(createProductRequest.name())
            .price(createProductRequest.price())
            .quantity(createProductRequest.quantity())
            .userId(authToken.getPrincipal().userId())
            .build();
    return repository.save(product);
  }

  @GetMapping("/products/{id}")
  Product one(@PathVariable UUID id) {
    return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
  }

  public record CreateProductRequest(String name, BigDecimal price, int quantity) {}
}
