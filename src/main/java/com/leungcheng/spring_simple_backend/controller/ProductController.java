package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.domain.Product;
import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    Product newProduct(@Valid @RequestBody Product product) {
        return repository.save(product);
    }

    @GetMapping("/products/{id}")
    Product one(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("TODO"));
    }

}
