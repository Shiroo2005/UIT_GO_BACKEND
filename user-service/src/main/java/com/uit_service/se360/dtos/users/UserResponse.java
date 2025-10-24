package com.uit_service.se360.dtos.users;

import com.uit_service.se360.entities.BaseEntity;
import com.uit_service.se360.enums.UserRole;
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
