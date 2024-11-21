package com.leungcheng.spring_simple_backend.domain;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtService {
  public record UserInfo(String userId) {}

  public record Config(String hs256Key) {}

  public JwtService(Config config) {
    byte[] keyBytes = config.hs256Key().getBytes(StandardCharsets.UTF_8);
    secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  private final SecretKey secretKey;

  public String generateAccessToken(User user) {
    return Jwts.builder().subject(user.getId()).signWith(this.secretKey, Jwts.SIG.HS256).compact();
  }

  public UserInfo parseAccessToken(String token) {
    String userId =
        Jwts.parser()
            .verifyWith(this.secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    return new UserInfo(userId);
  }
}
