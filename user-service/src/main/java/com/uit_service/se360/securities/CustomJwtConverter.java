package com.uit_service.se360.securities;

import com.uit_service.se360.entities.User;
import com.uit_service.se360.repositories.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserRepository userRepository;

  // private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {

    String userId = jwt.getSubject();
    User user =
        userRepository.findById(userId).orElseThrow(() -> new JwtException("User not found"));

    CustomUserDetails principal =
        CustomUserDetails.builder()
            .id(user.getId())
            .email(user.getEmail())
            .authorities(Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
            .build();
    return new UsernamePasswordAuthenticationToken(principal, jwt, principal.getAuthorities());
  }
}
