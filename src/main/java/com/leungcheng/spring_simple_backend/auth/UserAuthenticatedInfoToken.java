package com.leungcheng.spring_simple_backend.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthenticatedInfoToken extends AbstractAuthenticationToken {
  private final UserAuthenticatedInfo userAuthenticatedInfo;

  public UserAuthenticatedInfoToken(UserAuthenticatedInfo userAuthenticatedInfo) {
    super(null);
    this.userAuthenticatedInfo = userAuthenticatedInfo;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public String getPrincipal() {
    return userAuthenticatedInfo.userId();
  }
}
