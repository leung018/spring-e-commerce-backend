package com.leungcheng.spring_simple_backend.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

class ObjectValidatorTest {
  private final Dummy2 dummy2 = new Dummy2();

  @Test
  void shouldThrowException_WhenNotMatchTheConstraint() {
    dummy2.myName = "";
    ObjectValidator.ObjectValidationException exception =
        assertThrows(
            ObjectValidator.ObjectValidationException.class,
            () -> ObjectValidator.validate(dummy2));
    assertEquals("must not be blank", exception.getFirstErrorMessage());
    assertEquals("myName", exception.getFirstErrorField());
  }
}

class Dummy2 {
  @NotBlank(message = "must not be blank")
  String myName;
}
