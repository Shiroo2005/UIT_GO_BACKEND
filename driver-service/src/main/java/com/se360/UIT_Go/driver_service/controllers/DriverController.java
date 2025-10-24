package com.se360.UIT_Go.driver_service.controllers;


import com.se360.UIT_Go.driver_service.clients.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {

    private final UserClient userClient;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String getUsers( ){

        return "This is driver service" + " ____ User service tell: " + userClient.getUser() + " !. Done";
    }
}
