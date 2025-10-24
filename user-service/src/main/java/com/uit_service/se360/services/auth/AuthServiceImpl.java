package com.uit_service.se360.services.auth;

import com.uit_service.se360.dtos.auth.LoginRequest;
import com.uit_service.se360.dtos.auth.LoginResponse;
import com.uit_service.se360.securities.SecurityUtil;
import com.uit_service.se360.securities.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Override
  public LoginResponse login(LoginRequest request) {
    // 1. ---- Authenticate ----
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 2. ---- Set to security holder  ----
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 3. ---- Generate JWT ----
    String userId = SecurityUtil.getCurrentUserId();
    String jwt = tokenProvider.generateAccessToken(userId);

    // 4. ---- Response ----
    LoginResponse response = new LoginResponse();
    response.setAccessToken(jwt);
    return response;
  }
}
