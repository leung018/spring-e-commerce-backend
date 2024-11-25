package com.leungcheng.spring_simple_backend.domain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
  public record UserInfo(String userId) {}

  public static class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
      super(message);
    }
  }

  public JwtService(
      @Value("${jwt.hs256Key}") String hs256Key,
      @Value("${jwt.expiredDuration}") Duration expiredDuration) {
    byte[] keyBytes = hs256Key.getBytes(StandardCharsets.UTF_8);
    secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    this.expiredDuration = expiredDuration;
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

  /**
   * @throws InvalidTokenException if token is invalid due to expiration, invalid signature, or
   *     other reasons
   */
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
      if (e instanceof SignatureException) {
        throw new InvalidTokenException("Invalid signature");
      }
      throw new InvalidTokenException("Invalid token");
    }
  }
}
