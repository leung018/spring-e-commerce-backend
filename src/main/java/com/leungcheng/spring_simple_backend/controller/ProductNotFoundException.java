package com.leungcheng.spring_simple_backend.controller;

class ProductNotFoundException extends RuntimeException {
    ProductNotFoundException(String id) {
        super("Could not find product " + id);
    }
}
