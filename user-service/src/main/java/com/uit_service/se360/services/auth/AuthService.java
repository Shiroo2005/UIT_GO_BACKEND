package com.uit_service.se360.services.auth;

import com.uit_service.se360.dtos.auth.LoginRequest;
import com.uit_service.se360.dtos.auth.LoginResponse;
import com.uit_service.se360.dtos.auth.RefreshTokenRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
