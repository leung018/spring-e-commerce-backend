package com.leungcheng.spring_simple_backend.domain;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
