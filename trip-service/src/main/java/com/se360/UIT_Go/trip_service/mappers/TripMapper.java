package com.se360.UIT_Go.trip_service.mappers;

import com.se360.UIT_Go.trip_service.dtos.TripRequest;
import com.se360.UIT_Go.trip_service.dtos.TripResponse;
import com.se360.UIT_Go.trip_service.entities.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripMapper {
    Trip requestToEntity(TripRequest request);

    TripResponse entityToResponse(Trip trip);

    void partialUpdate(TripRequest request, @MappingTarget Trip trip);
}
