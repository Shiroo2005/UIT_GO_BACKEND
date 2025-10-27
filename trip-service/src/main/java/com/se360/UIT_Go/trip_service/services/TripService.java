package com.se360.UIT_Go.trip_service.services;

import com.se360.UIT_Go.trip_service.dtos.TripRequest;
import com.se360.UIT_Go.trip_service.dtos.TripResponse;
import com.se360.UIT_Go.trip_service.entities.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;

public interface TripService {
    Page<TripResponse> getAll(Pageable pageable, Specification<Trip> specification);

    TripResponse getById(String tripId);

    TripResponse create(TripRequest request, Jwt jwt);

    TripResponse driverAcceptTrip(String tripId, Jwt jwt);

    TripResponse driverStartTrip(String tripId, Jwt jwt);

    void cancelTrip(String tripId, Jwt jwt);
}
