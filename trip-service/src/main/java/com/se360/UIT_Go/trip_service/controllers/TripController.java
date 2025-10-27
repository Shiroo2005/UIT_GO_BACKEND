package com.se360.UIT_Go.trip_service.controllers;

import com.se360.UIT_Go.trip_service.clients.UserClient;
import com.se360.UIT_Go.trip_service.dtos.TripRequest;
import com.se360.UIT_Go.trip_service.dtos.TripResponse;
import com.se360.UIT_Go.trip_service.entities.Trip;
import com.se360.UIT_Go.trip_service.services.TripService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;


    @GetMapping
    public ResponseEntity<Page<TripResponse>> getAll(@ParameterObject Pageable pageable, @RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "all", required = false) boolean all) {
        if (all) {
            pageable = Pageable.unpaged();
        }
        Specification<Trip> specification = RSQLJPASupport.toSpecification(filter);
        return ResponseEntity.ok(tripService.getAll(pageable, specification));
    }

    @PostMapping
    public ResponseEntity<TripResponse> create(@Valid @RequestBody TripRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(tripService.create(request, jwt));
    }


    @PostMapping("/{id}/accept")
    public ResponseEntity<TripResponse> driverAcceptTrip(@PathVariable("id") String tripId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(tripService.driverAcceptTrip(tripId, jwt));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TripResponse> driverStartTrip(@PathVariable("id") String tripId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(tripService.driverStartTrip(tripId, jwt));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> passengerCancelTrip(@PathVariable("id") String tripId, @AuthenticationPrincipal Jwt jwt) {
        tripService.cancelTrip(tripId, jwt);
        return ResponseEntity.ok(null);
    }
}
