package com.se360.UIT_Go.user_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/users")
public class UserController {
    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public String getUsers() {
        return "This is user service";
    }
}
