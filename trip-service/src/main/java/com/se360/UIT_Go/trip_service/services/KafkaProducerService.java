package com.se360.UIT_Go.trip_service.services;

import com.se360.UIT_Go.trip_service.events.DriverAcceptTripMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final String TOPIC = "trip-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTripAcceptMessage(DriverAcceptTripMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }

    @KafkaListener(topics = TOPIC, groupId = "mygroup")
    public void receiveMessage(DriverAcceptTripMessage message) {
        System.out.println("Receive from kafka: " + message.getDriverId());
    }
}
