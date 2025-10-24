package com.uit_service.se360.securities;

import com.uit_service.se360.repositories.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
    var user =
        userRepository
            .findOne((root, _, builder) -> builder.equal(root.get("email"), credential))
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User not found with email or phone: " + credential));
    return CustomUserDetails.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPasswordHash())
        .role(user.getRole())
        .authorities(Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
        .build();
  }
}
