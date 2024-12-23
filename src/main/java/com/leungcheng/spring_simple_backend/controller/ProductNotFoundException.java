package com.leungcheng.spring_simple_backend.controller;

import java.util.UUID;

class ProductNotFoundException extends RuntimeException {
  ProductNotFoundException(UUID id) {
    super("Could not find product " + id);
  }
}
