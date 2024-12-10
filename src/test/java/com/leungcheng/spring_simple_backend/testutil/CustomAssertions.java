package com.leungcheng.spring_simple_backend.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class CustomAssertions {
  public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
    assertEquals(0, expected.compareTo(actual));
  }
}
