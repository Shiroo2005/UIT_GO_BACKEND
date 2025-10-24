package com.uit_service.se360.services.auth;

import com.uit_service.se360.dtos.auth.LoginRequest;
import com.uit_service.se360.dtos.auth.LoginResponse;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);
}
