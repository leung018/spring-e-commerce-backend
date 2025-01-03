package com.leungcheng.spring_e_commerce_backend.domain;

import static com.leungcheng.spring_e_commerce_backend.testutil.DefaultBuilders.userBuilder;
import static org.junit.jupiter.api.Assertions.*;

import com.leungcheng.spring_e_commerce_backend.validation.ObjectValidator;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void shouldCreateUser() {
    User user =
        userBuilder().username("user_1").password("password").balance(new BigDecimal(25)).build();

    assertEquals("user_1", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals(new BigDecimal(25), user.getBalance());
  }

  @Test
  void shouldIdIsDifferentForEachBuild() {
    User user1 = userBuilder().build();
    User user2 = userBuilder().build();

    assertNotEquals(user1.getId(), user2.getId());
  }

  @Test
  void shouldToBuilderAbleToBuildSameUser() {
    User user1 = userBuilder().build();
    User user2 = user1.toBuilder().build();

    assertEquals(user1.getId(), user2.getId());
    assertEquals(user1.getUsername(), user2.getUsername());
    assertEquals(user1.getPassword(), user2.getPassword());
    assertEquals(user1.getBalance(), user2.getBalance());
  }

  @Test
  void shouldRaiseExceptionWhenBuild_IfParamsViolateTheValidationConstraints() {
    assertThrowValidationException(userBuilder().username(null));
    assertThrowValidationException(userBuilder().username(""));

    assertThrowValidationException(userBuilder().password(null));
    assertThrowValidationException(userBuilder().password(""));

    assertThrowValidationException(userBuilder().balance(new BigDecimal("-1")));
    userBuilder().balance(BigDecimal.ZERO).build();
  }

  private void assertThrowValidationException(User.Builder builder) {
    Class<ObjectValidator.ObjectValidationException> expected =
        ObjectValidator.ObjectValidationException.class;
    assertThrows(expected, builder::build);
  }
}
