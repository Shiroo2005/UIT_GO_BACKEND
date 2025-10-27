package com.se360.UIT_Go.trip_service.clients.dtos;

import com.se360.UIT_Go.trip_service.entities.BaseEntity;
import com.se360.UIT_Go.trip_service.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseEntity {
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
    private DriverProfileResponse driverProfile;
}
