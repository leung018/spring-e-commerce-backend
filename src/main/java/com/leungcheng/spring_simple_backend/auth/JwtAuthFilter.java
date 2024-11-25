package com.leungcheng.spring_simple_backend.auth;

import com.leungcheng.spring_simple_backend.domain.JwtService;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Autowired private JwtService jwtService;
  @Autowired private UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    getAccessToken(authHeader)
        .ifPresent(
            accessToken -> {
              if (SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                  UserInfoAuthenticationToken authToken =
                      new UserInfoAuthenticationToken(jwtService.parseAccessToken(accessToken));
                  SecurityContext context = SecurityContextHolder.createEmptyContext();
                  context.setAuthentication(authToken);
                  SecurityContextHolder.setContext(context);
                } catch (JwtService.InvalidTokenException e) {
                  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
              }
            });

    filterChain.doFilter(request, response);
  }

  private Optional<String> getAccessToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return Optional.empty();
    }

    String token = authHeader.substring(7);
    if (token.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(token);
  }
}
