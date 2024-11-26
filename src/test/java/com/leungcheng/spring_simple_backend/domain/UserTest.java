package com.leungcheng.spring_simple_backend.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
  private static User.Builder userBuilder() {
    return new User.Builder().username("Default User").password("default_password").balance(0);
  }

  @Test
  void shouldCreateUser() {
    User user = userBuilder().username("User 1").password("password").balance(100).build();

    assertEquals("User 1", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals(100, user.getBalance());
  }

  @Test
  void shouldIdIsDifferentForEachBuild() {
    User user1 = userBuilder().build();
    User user2 = userBuilder().build();

    assertNotEquals(user1.getId(), user2.getId());
  }
}
