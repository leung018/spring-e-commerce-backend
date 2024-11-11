package com.leungcheng.spring_simple_backend.controller;

class UsernameAlreadyExistsException extends RuntimeException {
  UsernameAlreadyExistsException(String username) {
    super("Username " + username + " already exists");
  }
}
