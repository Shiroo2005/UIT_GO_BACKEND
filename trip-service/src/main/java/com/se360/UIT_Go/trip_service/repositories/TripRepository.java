package com.se360.UIT_Go.trip_service.repositories;

import com.se360.UIT_Go.trip_service.entities.Trip;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends SimpleRepository<Trip, String> {
}
