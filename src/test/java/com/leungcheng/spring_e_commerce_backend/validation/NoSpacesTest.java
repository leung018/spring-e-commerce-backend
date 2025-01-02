package com.leungcheng.spring_e_commerce_backend.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NoSpacesTest {
  private final Dummy dummy = new Dummy();

  @Test
  void shouldNotThrowException_WhenStringHasNoSpaces() {
    dummy.name = "NoSpaces";
    ObjectValidator.validate(dummy);
  }

  @Test
  void shouldThrowException_WhenStringHasSpaces() {
    dummy.name = "Has Spaces";
    assertThrows(
        ObjectValidator.ObjectValidationException.class, () -> ObjectValidator.validate(dummy));
  }

  @Test
  void shouldNotThrowException_WhenStringIsNull() {
    dummy.name = null;
    ObjectValidator.validate(dummy);
  }
}

class Dummy {
  @NoSpaces String name;
}
