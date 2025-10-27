package com.se360.UIT_Go.trip_service.clients;

import com.se360.UIT_Go.trip_service.clients.dtos.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    UserResponse getUser(@PathVariable("id") String id);
}
