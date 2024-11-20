package com.leungcheng.spring_simple_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JwtServiceTest {
  private static User.Builder userBuilder() {
    return new User.Builder().username("default-user").password("password");
  }

  @Test
  void shouldGenerateAndParseAccessToken() {
    JwtService jwtService = new JwtService(JwtService.Config.sample());
    User user = userBuilder().build();

    String token = jwtService.generateAccessToken(user);
    JwtService.UserInfo userInfo = jwtService.parseAccessToken(token);

    assertEquals(user.getId(), userInfo.userId());
  }
}
