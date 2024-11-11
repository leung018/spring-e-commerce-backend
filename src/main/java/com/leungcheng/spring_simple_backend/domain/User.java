package com.leungcheng.spring_simple_backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  public static class Builder {
    private double balance = 0;
    private String username;
    private String password;

    public Builder balance(double balance) {
      this.balance = balance;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public User build() {
      User user = new User();
      user.balance = balance;
      user.username = username;
      user.password = password;
      ObjectValidator.validate(user);

      return user;
    }
  }

  private User() {}

  @Id
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String id = java.util.UUID.randomUUID().toString();

  @Min(0)
  private double balance;

  private String username;

  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(); // TODO: May be not return empty list
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }
}
