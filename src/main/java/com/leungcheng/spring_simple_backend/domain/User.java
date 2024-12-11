package com.leungcheng.spring_simple_backend.domain;

import com.leungcheng.spring_simple_backend.validation.ObjectValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  public static final BigDecimal INITIAL_BALANCE = new BigDecimal(100);

  public static class Builder {
    private String username;
    private String password;
    private BigDecimal balance;
    private String id = java.util.UUID.randomUUID().toString();

    private Builder id(String id) {
      this.id = id;
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

    public Builder balance(BigDecimal balance) {
      this.balance = balance;
      return this;
    }

    public User build() {
      User user = new User();

      user.id = id;
      user.username = username;
      user.password = password;
      user.balance = balance;
      ObjectValidator.validate(user);

      return user;
    }
  }

  private User() {}

  public User.Builder toBuilder() {
    return new User.Builder().username(username).password(password).balance(balance).id(id);
  }

  @Id private String id;

  @Column(unique = true)
  @NotBlank
  private String username;

  @NotBlank private String password;

  @Min(0)
  @Column(precision = 19, scale = 10)
  private BigDecimal balance;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(); // TODO: May be not return empty list
  }

  public String getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
