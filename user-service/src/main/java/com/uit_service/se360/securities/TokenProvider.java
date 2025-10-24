package com.uit_service.se360.securities;

import com.uit_service.se360.configs.JwtConfig;
import com.uit_service.se360.entities.User;
import com.uit_service.se360.repositories.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

  private final UserRepository userRepository;
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  @Value("${jwt.access-token.expiration}")
  private Long expirationTime;

  public String generateAccessToken(String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .claim("role", user.getRole().getName())
            .expiresAt(now.plusSeconds(expirationTime))
            .subject(user.getId().toString())
            .build();
    JwsHeader jwsHeader = JwsHeader.with(JwtConfig.JWT_ALGORITHM).build();
    return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  public Jwt validateJwt(String token) {
    try {
      Jwt jwt = jwtDecoder.decode(token);
      if (jwt.getExpiresAt().isBefore(Instant.now())) {
        throw new IllegalArgumentException("Token has expired");
      }
      return jwt;
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }
}
