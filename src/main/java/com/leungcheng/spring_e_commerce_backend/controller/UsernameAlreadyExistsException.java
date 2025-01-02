package com.leungcheng.spring_e_commerce_backend.controller;

class UsernameAlreadyExistsException extends RuntimeException {
  UsernameAlreadyExistsException(String username) {
    super("Username " + username + " already exists");
  }
}
