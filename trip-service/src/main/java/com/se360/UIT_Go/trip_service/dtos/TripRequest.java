package com.se360.UIT_Go.trip_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequest {

    @NotBlank(message = "Origin address must not be blank")
    private String originAddress;

    @NotNull(message = "Origin latitude must not be null")
    @DecimalMin(value = "-90.0", message = "Origin latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Origin latitude must be <= 90")
    private Double originLatitude;

    @NotNull(message = "Origin longitude must not be null")
    @DecimalMin(value = "-180.0", message = "Origin longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Origin longitude must be <= 180")
    private Double originLongitude;

    @NotBlank(message = "Destination address must not be blank")
    private String destinationAddress;

    @NotNull(message = "Destination latitude must not be null")
    @DecimalMin(value = "-90.0", message = "Destination latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Destination latitude must be <= 90")
    private Double destinationLatitude;

    @NotNull(message = "Destination longitude must not be null")
    @DecimalMin(value = "-180.0", message = "Destination longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Destination longitude must be <= 180")
    private Double destinationLongitude;
}
