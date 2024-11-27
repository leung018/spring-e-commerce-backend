package com.leungcheng.spring_simple_backend.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
  private static User.Builder userBuilder() {
    return new User.Builder().username("default_user").password("default_password");
  }

  @Test
  void shouldCreateUser() {
    User user = userBuilder().username("user_1").password("password").build();

    assertEquals("user_1", user.getUsername());
    assertEquals("password", user.getPassword());
  }

  @Test
  void shouldIdIsDifferentForEachBuild() {
    User user1 = userBuilder().build();
    User user2 = userBuilder().build();

    assertNotEquals(user1.getId(), user2.getId());
  }

  @Test
  void shouldRaiseExceptionWhenBuild_IfParamsViolateTheValidationConstraints() {
    assertThrowValidationException(userBuilder().username("1".repeat(4))); // min characters
    assertThrowValidationException(userBuilder().username("1".repeat(21))); // max characters
    assertThrowValidationException(userBuilder().username("i have space"));

    assertThrowValidationException(userBuilder().password(null));
  }

  private void assertThrowValidationException(User.Builder builder) {
    Class<ObjectValidator.ObjectValidationException> expected =
        ObjectValidator.ObjectValidationException.class;
    assertThrows(expected, builder::build);
  }
}
