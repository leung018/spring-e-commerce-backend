package com.leungcheng.spring_simple_backend.domain.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {}
