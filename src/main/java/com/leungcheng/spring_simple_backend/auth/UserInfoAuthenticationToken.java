package com.leungcheng.spring_simple_backend.auth;

import com.leungcheng.spring_simple_backend.domain.JwtService.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserInfoAuthenticationToken extends AbstractAuthenticationToken {
  private final UserInfo userInfo;

  public UserInfoAuthenticationToken(UserInfo userInfo) {
    super(null);
    this.userInfo = userInfo;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public String getPrincipal() {
    return userInfo.userId();
  }
}
