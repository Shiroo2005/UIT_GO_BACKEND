package com.se360.UIT_Go.trip_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequest {
    private String originAddress;
    @NotNull
    private Double originLatitude;
    @NotNull
    private Double originLongitude;

    private String destinationAddress;
    @NotNull
    private Double destinationLatitude;
    @NotNull
    private Double destinationLongitude;
}
