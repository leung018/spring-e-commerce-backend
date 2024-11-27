package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.validation.ObjectValidator.ObjectValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ExceptionHandlerAdvice {
  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String productNotFoundHandler(ProductNotFoundException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String usernameAlreadyExistsHandler(UsernameAlreadyExistsException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(ObjectValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String illegalArgumentHandler(IllegalArgumentException ex) {
    return ex.getMessage();
  }
}
