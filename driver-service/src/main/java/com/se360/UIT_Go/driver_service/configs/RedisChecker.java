package com.se360.UIT_Go.driver_service.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisChecker {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisChecker(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void checkConnection() {
        try {
            String pong = (String) redisTemplate.getConnectionFactory().getConnection().ping();
            System.out.println("Redis connection test: " + pong); // PONG
        } catch (Exception e) {
            System.err.println("Redis connection failed: " + e.getMessage());
        }
    }
}
