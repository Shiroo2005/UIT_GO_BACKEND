package com.se360.UIT_Go.trip_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class TripServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripServiceApplication.class, args);
	}

}
