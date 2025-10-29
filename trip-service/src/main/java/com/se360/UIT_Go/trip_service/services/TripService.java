package com.se360.UIT_Go.trip_service.services;

import com.se360.UIT_Go.trip_service.dtos.TripRequest;
import com.se360.UIT_Go.trip_service.dtos.TripResponse;
import com.se360.UIT_Go.trip_service.entities.Trip;
import com.se360.UIT_Go.trip_service.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TripService {
    Page<TripResponse> getAll(Pageable pageable, Specification<Trip> specification);

    TripResponse getById(String tripId);

    TripResponse create(TripRequest request, String userId, UserRole role);

    TripResponse driverAcceptTrip(String tripId, String userId, UserRole role);

    TripResponse driverStartTrip(String tripId, String userId, UserRole role);

    TripResponse driverCompleteTrip(String tripId, String userId, UserRole role);

    void cancelTrip(String tripId, String userId, UserRole role);
}
