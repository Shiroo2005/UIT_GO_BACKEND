package com.uit_service.se360.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  PASSENGER("PASSENGER"),
  DRIVER("DRIVER"),
  ADMIN("ADMIN");

  private final String name;
}
