package com.se360.UIT_Go.driver_service.controllers;


import com.se360.UIT_Go.driver_service.constants.Topic;
import com.se360.UIT_Go.driver_service.dto.DriverLocationRequest;
import com.se360.UIT_Go.driver_service.dto.SearchDriversNearRequest;
import com.se360.UIT_Go.driver_service.events.DriverAcceptTripMessage;
import com.se360.UIT_Go.driver_service.services.driver.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {

    private final IDriverService driverService;


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/location")
    public void updateDriverLocation(@RequestHeader("X-User-ID") String userId,
                                     @PathVariable("id") String driverId,
                                     @RequestBody() DriverLocationRequest driverLocationRequest) {

        if (!userId.equals(driverId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "USER ID NOT MATCH");

        this.driverService.updateLocation(userId, driverLocationRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/nears/{radiusKm}")
    public String[] searchDriverLocation(@PathVariable("radiusKm") Long radiusKm, @RequestBody() SearchDriversNearRequest searchDriverLocationRequest) {
        return this.driverService.searchDriversNear(searchDriverLocationRequest.getLat(), searchDriverLocationRequest.getLng(), radiusKm);
    }

    @KafkaListener(topics = Topic.ACCEPT_TRIP_TOPIC, groupId = "mygroup")
    public void driverAcceptTrip(DriverAcceptTripMessage message) {
        this.driverService.updateStatusDriverAcceptTrip(message.getDriverId());
    }

    @KafkaListener(topics = Topic.COMPLETE_TRIP_TOPIC, groupId = "mygroup")
    public void driverCompleteTrip(DriverAcceptTripMessage message) {
        this.driverService.updateStatusDriverOffTrip(message.getDriverId());
    }
}
