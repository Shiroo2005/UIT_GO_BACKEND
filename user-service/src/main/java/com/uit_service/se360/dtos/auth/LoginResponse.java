package com.uit_service.se360.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
  private String accessToken;
  private String refreshToken;
}
