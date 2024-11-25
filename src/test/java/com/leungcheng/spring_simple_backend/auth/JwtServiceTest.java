package com.leungcheng.spring_simple_backend.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.leungcheng.spring_simple_backend.domain.User;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class JwtServiceTest {
  private static User.Builder userBuilder() {
    return new User.Builder().username("default-user").password("password");
  }

  private static class JwtServiceBuilder {
    private String hs256Key = Jwts.SIG.HS256.key().build().toString();
    private Duration expiredDuration = Duration.ofHours(1);

    private JwtServiceBuilder expiredDuration(Duration expiredDuration) {
      this.expiredDuration = expiredDuration;
      return this;
    }

    private JwtServiceBuilder newHs256Key() {
      this.hs256Key = Jwts.SIG.HS256.key().build().toString();
      return this;
    }

    private JwtService build() {
      return new JwtService(hs256Key, expiredDuration);
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

  @Test
  void shouldThrowExceptionIfTokenExpired() {
    JwtService jwtService = new JwtServiceBuilder().expiredDuration(Duration.ofSeconds(-1)).build();
    User user = userBuilder().build();

    String token = jwtService.generateAccessToken(user);

    JwtService.InvalidTokenException exception =
        assertThrows(
            JwtService.InvalidTokenException.class,
            () -> {
              jwtService.parseAccessToken(token);
            });
    assertEquals("Expired token", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionIfTokenIsSignedByDifferentKey() {
    JwtServiceBuilder builder = new JwtServiceBuilder();
    JwtService jwtService = builder.build();
    User user = userBuilder().build();

    String token = jwtService.generateAccessToken(user);

    JwtService.InvalidTokenException exception =
        assertThrows(
            JwtService.InvalidTokenException.class,
            () -> {
              builder.newHs256Key().build().parseAccessToken(token);
            });
    assertEquals("Invalid signature", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionIfTokenIsInvalid() {
    JwtService jwtService = new JwtServiceBuilder().build();
    User user = userBuilder().build();

    JwtService.InvalidTokenException exception =
        assertThrows(
            JwtService.InvalidTokenException.class,
            () -> {
              jwtService.parseAccessToken("invalid");
            });
    assertEquals("Invalid token", exception.getMessage());
  }
}
