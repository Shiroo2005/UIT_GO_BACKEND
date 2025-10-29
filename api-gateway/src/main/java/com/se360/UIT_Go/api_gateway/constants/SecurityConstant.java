package com.se360.UIT_Go.api_gateway.constants;

public interface SecurityConstant {
    String[] PUBLIC_URLS = {
            "/user-service/auth/**",
            "/user-service/swagger-ui/**",
            "/user-service/v3/api-docs/**",
            "/trip-service/swagger-ui/**",
            "/trip-service/v3/api-docs/**",
            "/driver-service/v3/api-docs/**",
            "/driver-service/swagger-ui/**",
    };
}
