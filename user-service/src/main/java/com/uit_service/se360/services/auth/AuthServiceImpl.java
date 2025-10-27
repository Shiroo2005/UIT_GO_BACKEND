package com.uit_service.se360.services.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import com.uit_service.se360.dtos.auth.LoginRequest;
import com.uit_service.se360.dtos.auth.LoginResponse;
import com.uit_service.se360.dtos.auth.RefreshTokenRequest;
import com.uit_service.se360.entities.RefreshToken;
import com.uit_service.se360.repositories.RefreshTokenRepository;
import com.uit_service.se360.repositories.UserRepository;
import com.uit_service.se360.securities.SecurityUtil;
import com.uit_service.se360.securities.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

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


        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setId(UuidCreator.getTimeOrderedEpoch().toString());
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration));
        newRefreshToken.setUser(userRepository.getReferenceById(userId));
        newRefreshToken = refreshTokenRepository.save(newRefreshToken);

        // 4. ---- Response ----
        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(newRefreshToken.getToken());
        return response;
    }

    @Override
    public LoginResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findOne((root, _, builder) -> builder.equal(root.get("token"), request.getRefreshToken()))
                .orElseThrow(() -> new IllegalStateException("Invalid Refresh Token"));
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Expired Refresh Token");
        }
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setId(UuidCreator.getTimeOrderedEpoch().toString());
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration));
        newRefreshToken.setUser(refreshToken.getUser());
        newRefreshToken = refreshTokenRepository.save(newRefreshToken);

        String jwt = tokenProvider.generateAccessToken(refreshToken.getUser().getId());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(newRefreshToken.getToken());
        return response;
    }

    @Override
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.delete((root, _, builder) -> builder.equal(root
                .get("token"), request.getRefreshToken()));
    }
}
