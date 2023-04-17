package com.xuanluan.mc.cloud.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author Xuan Luan
 * @createdAt 1/25/2023
 */
@Service
public class SwaggerService {

    public Mono<Void> redirectSwaggerUI(String token, ServerHttpResponse response, String location) {
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().set("X-CSRFToken", token);
        response.getHeaders().setLocation(URI.create(location));
        return response.setComplete();
    }
}
