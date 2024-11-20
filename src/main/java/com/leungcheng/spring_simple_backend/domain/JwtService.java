package com.leungcheng.spring_simple_backend.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import javax.crypto.SecretKey;

public class JwtService {
  public record UserInfo(String userId) {}

  public record Config(SecretKey secretKey, MacAlgorithm algorithm) {
    public static Config sample() {
      MacAlgorithm alg = Jwts.SIG.HS256;
      SecretKey key = alg.key().build();
      return new Config(key, alg);
    }
  }

  private final Config config;

  public JwtService(Config config) {
    this.config = config;
  }

  public String generateAccessToken(User user) {
    return Jwts.builder()
        .subject(user.getId())
        .signWith(config.secretKey(), Jwts.SIG.HS256)
        .compact();
  }

  public UserInfo parseAccessToken(String token) {
    String userId =
        Jwts.parser()
            .verifyWith(config.secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    return new UserInfo(userId);
  }
}
