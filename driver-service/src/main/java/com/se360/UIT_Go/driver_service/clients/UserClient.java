package com.se360.UIT_Go.driver_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/hello")
    String getUser();
}
