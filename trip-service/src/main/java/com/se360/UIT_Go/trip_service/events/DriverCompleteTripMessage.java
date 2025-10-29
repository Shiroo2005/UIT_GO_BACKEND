package com.se360.UIT_Go.trip_service.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverCompleteTripMessage {
    private String driverId;
}
