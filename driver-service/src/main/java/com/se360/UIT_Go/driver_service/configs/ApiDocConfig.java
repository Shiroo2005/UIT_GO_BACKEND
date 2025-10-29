package com.se360.UIT_Go.driver_service.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ApiDocConfig {

    private final DiscoveryClient discoveryClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }

    @Bean
    OpenAPI openApi() {
        List<Server> servers = discoveryClient.getInstances("api-gateway").stream().map(
                v -> new Server().url(v.getUri().toString() + contextPath)
        ).toList();
        return new OpenAPI()
                .servers(servers)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", createAPIKeyScheme()));
    }
}
