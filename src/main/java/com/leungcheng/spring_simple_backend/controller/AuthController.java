package com.leungcheng.spring_simple_backend.controller;

import com.leungcheng.spring_simple_backend.auth.JwtService;
import com.leungcheng.spring_simple_backend.domain.User;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import com.leungcheng.spring_simple_backend.validation.NoSpaces;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
  @Autowired private JwtService JwtService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void signup(@Valid @RequestBody AuthController.UserCredentials userCredentials) {
    String hashedPassword = passwordEncoder.encode(userCredentials.password());
    User user =
        new User.Builder().username(userCredentials.username()).password(hashedPassword).build();
    try {
      this.userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new UsernameAlreadyExistsException(userCredentials.username());
    }
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public LoginResponse login(@Valid @RequestBody AuthController.UserCredentials userCredentials) {
    Authentication authenticationRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(
            userCredentials.username(), userCredentials.password());
    this.authenticationManager.authenticate(authenticationRequest);
    User user = this.userRepository.findByUsername(userCredentials.username()).orElseThrow();
    String accessToken = JwtService.generateAccessToken(user);
    return new LoginResponse(accessToken);
  }

  public record UserCredentials(
      @Size(min = 5, max = 20) @Pattern(regexp = "^[a-z0-9]+$") String username,
      @Size(min = 8, max = 50) @NoSpaces String password) {}

  public record LoginResponse(String accessToken) {}
}
