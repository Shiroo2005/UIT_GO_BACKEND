package com.se360.UIT_Go.trip_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final String TOPIC = "trip-topic";
    private final KafkaTemplate<String, Map<String,String>> kafkaTemplate;

    public void sendTripAcceptEvent(String tripId) {
        var message = Map.of("tripId",tripId);
        kafkaTemplate.send(TOPIC, message);
    }

    @KafkaListener(topics = TOPIC, groupId = "mygroup")
    public void receiveMessage(Map<String,String> message) {
        var a = message.get("tripId");
        System.out.println("Receive from kafka: " + a);
    }
}
