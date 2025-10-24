package com.se360.UIT_Go.trip_service.controllers;

import com.se360.UIT_Go.trip_service.clients.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripController {

    private final UserClient userClient;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String getUsers( ){

        return "This is trip service" + " ____ User service tell: " + userClient.getUser() + " !. Done";
    }
}
