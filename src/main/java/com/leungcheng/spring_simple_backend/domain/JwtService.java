package com.leungcheng.spring_simple_backend.domain;

public class JwtService {
  public record UserInfo(String userId) {}

  public String generateToken(User user) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public UserInfo parseToken(String token) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
