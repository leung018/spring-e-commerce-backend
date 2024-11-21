package com.leungcheng.spring_simple_backend.domain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtService {
  public record UserInfo(String userId) {}

  public record Config(String hs256Key, Duration expiredDuration) {}

  public static class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
      super(message);
    }
  }

  public JwtService(Config config) {
    byte[] keyBytes = config.hs256Key().getBytes(StandardCharsets.UTF_8);
    secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    expiredDuration = config.expiredDuration();
  }

  private final SecretKey secretKey;

  private final Duration expiredDuration;

  private Date getExpirationDate() {
    return Date.from(Instant.now().plus(this.expiredDuration));
  }

  public String generateAccessToken(User user) {
    return Jwts.builder()
        .subject(user.getId())
        .expiration(getExpirationDate())
        .signWith(this.secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public UserInfo parseAccessToken(String token) {
    try {
      String userId =
          Jwts.parser()
              .verifyWith(this.secretKey)
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();
      return new UserInfo(userId);
    } catch (Exception e) {
      if (e instanceof ExpiredJwtException) {
        throw new InvalidTokenException("Expired token");
      }
      throw new InvalidTokenException("Invalid token");
    }
  }
}
