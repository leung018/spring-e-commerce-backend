package com.leungcheng.spring_simple_backend.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class ObjectValidator {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  static <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<T> violation : violations) {
        sb.append(violation.getMessage()).append("\n");
      }
      throw new IllegalArgumentException("Validation failed:\n" + sb);
    }
  }
}
