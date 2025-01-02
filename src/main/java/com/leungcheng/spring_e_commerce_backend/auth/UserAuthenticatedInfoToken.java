package com.leungcheng.spring_e_commerce_backend.auth;

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
  public UserAuthenticatedInfo getPrincipal() {
    return userAuthenticatedInfo;
  }
}
