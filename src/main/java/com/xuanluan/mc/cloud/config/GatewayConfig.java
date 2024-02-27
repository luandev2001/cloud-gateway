package com.xuanluan.mc.cloud.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;

/**
 * @author Xuan Luan
 * @createdAt 10/9/2022
 */
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("swagger-service",
                        r -> r.path("/swagger-service/**")
                                .uri("http://localhost:8081"))
                .route("auth-service",
                        r -> r.path("/auth-service/**")
                                .uri("http://localhost:8082"))
                .route("client-service",
                        r -> r.path("/client-service/**")
                                .uri("http://localhost:8083"))
                .route("org-service",
                        r -> r.path("/org-service/**")
                                .uri("http://localhost:8084"))
                .route("product-service",
                        r -> r.path("/product-service/**")
                                .uri("http://localhost:8085"))
                .build();
    }

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }
}
