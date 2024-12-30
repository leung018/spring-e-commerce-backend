package com.leungcheng.spring_simple_backend.domain;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {}
