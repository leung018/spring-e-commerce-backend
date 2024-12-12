package com.leungcheng.spring_simple_backend.validation;

public class MyIllegalArgumentException extends IllegalArgumentException {
  public MyIllegalArgumentException(String message) {
    super(message);
  }
}
