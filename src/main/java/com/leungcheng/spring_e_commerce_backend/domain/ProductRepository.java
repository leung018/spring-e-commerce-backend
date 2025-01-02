package com.leungcheng.spring_e_commerce_backend.domain;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {}
