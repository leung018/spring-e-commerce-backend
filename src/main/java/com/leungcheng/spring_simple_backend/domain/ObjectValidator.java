package com.leungcheng.spring_simple_backend.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class ObjectValidator {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static class ObjectValidationException extends IllegalArgumentException {
    public ObjectValidationException(String message) {
      super(message);
    }
  }

  static <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<T> violation : violations) {
        sb.append(violation.getMessage()).append("\n");
      }
      throw new ObjectValidationException("Validation failed:\n" + sb);
    }
  }
}
