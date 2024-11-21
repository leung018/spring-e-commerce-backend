package com.leungcheng.spring_simple_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

public class JwtServiceTest {
  private static User.Builder userBuilder() {
    return new User.Builder().username("default-user").password("password");
  }

  private static class JwtServiceBuilder {
    private final String hs256Key = Jwts.SIG.HS256.key().build().toString();

    private JwtService build() {
      return new JwtService(new JwtService.Config(hs256Key));
    }
  }

  @Test
  void shouldGenerateAndParseAccessToken() {
    JwtService jwtService = new JwtServiceBuilder().build();
    User user = userBuilder().build();

    String token = jwtService.generateAccessToken(user);
    JwtService.UserInfo userInfo = jwtService.parseAccessToken(token);

    assertEquals(user.getId(), userInfo.userId());
  }
}
