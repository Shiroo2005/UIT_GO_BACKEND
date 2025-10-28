package com.se360.UIT_Go.trip_service.services;

import com.se360.UIT_Go.trip_service.dtos.TripRequest;
import com.se360.UIT_Go.trip_service.dtos.TripResponse;
import com.se360.UIT_Go.trip_service.entities.Trip;
import com.se360.UIT_Go.trip_service.enums.TripStatus;
import com.se360.UIT_Go.trip_service.enums.UserRole;
import com.se360.UIT_Go.trip_service.mappers.TripMapper;
import com.se360.UIT_Go.trip_service.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.github.f4b6a3.uuid.UuidCreator;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Override
    public Page<TripResponse> getAll(Pageable pageable, Specification<Trip> specification) {
        Page<Trip> tripPage = tripRepository.findAll(specification, pageable);
        return tripPage.map(tripMapper::entityToResponse);
    }

    @Override
    public TripResponse getById(String tripId) {
        return tripMapper.entityToResponse(tripRepository.findById(tripId).orElseThrow(() -> new IllegalStateException("Trip not found")));
    }

    @Override
    public TripResponse create(TripRequest request, String userId, UserRole role) {
        Trip trip = tripMapper.requestToEntity(request);
        trip.setId(UuidCreator.getTimeOrderedEpoch().toString());
        trip.setPassengerId(userId);
        trip.setStatus(TripStatus.SEARCHING);
        // Calculate fee
        trip = tripRepository.save(trip);
        return tripMapper.entityToResponse(trip);
    }

    @Override
    public TripResponse driverAcceptTrip(String tripId, String userId, UserRole userRole) {
        if (userRole != UserRole.DRIVER) {
            throw new IllegalStateException("Only driver can accept trip");
        }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        if (trip.getStatus() != TripStatus.SEARCHING) {
            throw new IllegalStateException("Trip is not available to accept");
        }
        trip.setDriverId(userId);
        trip.setStatus(TripStatus.ACCEPTED);
        return tripMapper.entityToResponse(trip);
    }

    @Override
    public TripResponse driverStartTrip(String tripId, String userId, UserRole role) {
        if (role != UserRole.DRIVER) {
            throw new IllegalStateException("Only driver can start trip");
        }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        if (trip.getStatus() != TripStatus.ACCEPTED) {
            throw new IllegalStateException("Trip is not accepted");
        }
        trip.setDriverId(userId);
        trip.setStatus(TripStatus.ONGOING);
        return tripMapper.entityToResponse(trip);
    }

    @Override
    public void cancelTrip(String tripId, String userId, UserRole role) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        if (trip.getStatus() != TripStatus.SEARCHING && trip.getStatus() != TripStatus.ACCEPTED) {
            throw new IllegalStateException("Trip is not available to cancel");
        }
        if (!Objects.equals(trip.getPassengerId(), userId)) {
            throw new IllegalStateException("Passenger doesn't have permission to cancel");
        }
        trip.setStatus(TripStatus.CANCELED);
        trip.setCanceledAt(Instant.now());
        trip = tripRepository.save(trip);
    }

}
