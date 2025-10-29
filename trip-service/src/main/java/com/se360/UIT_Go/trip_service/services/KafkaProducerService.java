package com.se360.UIT_Go.trip_service.services;

import com.se360.UIT_Go.trip_service.events.DriverAcceptTripMessage;
import com.se360.UIT_Go.trip_service.events.DriverCompleteTripMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final String ACCEPT_TRIP_TOPIC = "accept-trip-topic";
    private final String COMPLETE_TRIP_TOPIC = "complete-trip-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTripAcceptMessage(DriverAcceptTripMessage message) {
        kafkaTemplate.send(ACCEPT_TRIP_TOPIC, message);
    }

    public void sendTripCompleteMessage(DriverCompleteTripMessage message) {
        kafkaTemplate.send(COMPLETE_TRIP_TOPIC, message);
    }

    // test
    @KafkaListener(topics = ACCEPT_TRIP_TOPIC, groupId = "mygroup")
    public void receiveMessage(DriverAcceptTripMessage message) {
        System.out.println("Receive from kafka: " + message.getDriverId());
    }
}
