package com.uit_service.se360.dtos.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverProfileRequest {

    @NotBlank(message = "License plate must not be blank")
    private String licensePlate;

    @NotBlank(message = "Vehicle model must not be blank")
    private String vehicleModel;

    @NotBlank(message = "Vehicle color must not be blank")
    private String vehicleColor;
}
