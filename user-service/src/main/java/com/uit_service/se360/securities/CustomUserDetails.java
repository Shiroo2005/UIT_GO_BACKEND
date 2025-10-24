package com.uit_service.se360.securities;

import com.uit_service.se360.enums.UserRole;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
public class CustomUserDetails implements UserDetails {
  private String id;
  private String password;
  private String email;
  private UserRole role;

  private final Set<? extends GrantedAuthority> authorities;

  @Override
  public String getUsername() {
    return String.valueOf(id);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
