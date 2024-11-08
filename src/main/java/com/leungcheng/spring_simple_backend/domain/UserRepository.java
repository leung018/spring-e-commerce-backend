package com.leungcheng.spring_simple_backend.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
  User findByUsername(String username);
}
