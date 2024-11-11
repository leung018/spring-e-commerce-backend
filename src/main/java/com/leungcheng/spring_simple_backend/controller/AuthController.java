package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.domain.User;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void signup(@Valid @RequestBody LoginRequest loginRequest) {
    String hashedPassword = passwordEncoder.encode(loginRequest.password());
    User user =
        new User.Builder().username(loginRequest.username()).password(hashedPassword).build();
    this.userRepository.save(user);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public void login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authenticationRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(), loginRequest.password());
    this.authenticationManager.authenticate(authenticationRequest);
  }

  public record LoginRequest(String username, String password) {}
}
