package com.se360.UIT_Go.trip_service.dtos;

import com.se360.UIT_Go.trip_service.enums.TripStatus;
import com.se360.UIT_Go.trip_service.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TripResponse extends BaseEntity {
    private String driverId;
    private String passengerId;
    private TripStatus status;
    private String originAddress;
    private Double originLatitude;
    private Double originLongitude;
    private String destinationAddress;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Double estimateFee;
    private Instant pickupAt;
    private Instant completedAt;
    private Instant canceledAt;
}
