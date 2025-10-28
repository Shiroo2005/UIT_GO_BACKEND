package com.se360.UIT_Go.trip_service.controllers;

import com.se360.UIT_Go.trip_service.services.KafkaProducerService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestKafkaController {
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/test-kafka")
    public ResponseEntity<String> testKafka() {
        kafkaProducerService.sendTripAcceptEvent("Xin chao");
        return ResponseEntity.ok("Success");
    }

}
