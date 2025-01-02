package com.leungcheng.spring_e_commerce_backend.validation;

public class MyIllegalArgumentException extends IllegalArgumentException {
  public MyIllegalArgumentException(String message) {
    super(message);
  }
}
