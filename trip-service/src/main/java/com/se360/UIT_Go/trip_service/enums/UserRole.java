package com.se360.UIT_Go.trip_service.enums;

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
