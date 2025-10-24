package com.uit_service.se360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Se360Application {

	public static void main(String[] args) {
		SpringApplication.run(Se360Application.class, args);
	}

}
