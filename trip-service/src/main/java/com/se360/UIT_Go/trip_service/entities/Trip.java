package com.se360.UIT_Go.trip_service.entities;

import com.se360.UIT_Go.trip_service.enums.TripStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "trips")
public class Trip extends BaseEntity {
    // normal info
    private String driverId;
    @Column(nullable = false)
    private String passengerId;

    // status info
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    // pickup address
    private String originAddress;
    @Column(nullable = false)
    private Double originLatitude;
    @Column(nullable = false)
    private Double originLongitude;

    // destination address
    private String destinationAddress;
    @Column(nullable = false)
    private Double destinationLatitude;
    @Column(nullable = false)
    private Double destinationLongitude;

    // price
    private Double estimateFee;

    private Instant pickupAt;
    private Instant completedAt;
    private Instant canceledAt;
}
