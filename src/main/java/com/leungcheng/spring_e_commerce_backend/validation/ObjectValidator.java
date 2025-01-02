package com.leungcheng.spring_e_commerce_backend.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectValidator {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static class ObjectValidationException extends IllegalArgumentException {
    private final List<FieldError> fieldErrors;

    ObjectValidationException(List<FieldError> fieldErrors) {
      super("Validation failed");
      this.fieldErrors = fieldErrors;
    }

    public String getFirstErrorField() {
      return fieldErrors.getFirst().field;
    }

    public String getFirstErrorMessage() {
      return fieldErrors.getFirst().message;
    }
  }

  private record FieldError(String field, String message) {}

  public static <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);
    if (!violations.isEmpty()) {
      List<FieldError> fieldErrors = new ArrayList<>();
      for (ConstraintViolation<T> violation : violations) {
        fieldErrors.add(
            new FieldError(violation.getPropertyPath().toString(), violation.getMessage()));
      }

      throw new ObjectValidationException(fieldErrors);
    }
  }
}
