package com.se360.UIT_Go.driver_service.dto;

import com.se360.UIT_Go.driver_service.constants.DriverStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverLocationRequest {

    @NotNull(message = "Latitude must not be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double lat;

    @NotNull(message = "Longitude must not be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double lng;

    @NotNull(message = "Driver status must not be null")
    private DriverStatus status;
}
