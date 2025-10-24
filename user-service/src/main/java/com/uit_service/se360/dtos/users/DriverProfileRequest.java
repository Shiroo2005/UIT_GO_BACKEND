package com.uit_service.se360.dtos.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverProfileRequest {
  @NotBlank private String licensePlate;

  @NotBlank private String vehicleModel;

  @NotBlank private String vehicleColor;
}
