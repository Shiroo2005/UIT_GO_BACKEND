package com.se360.UIT_Go.driver_service.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverAcceptTripMessage {
    private String driverId;
    private Double originLongitude;
    private Double originLatitude;
}
