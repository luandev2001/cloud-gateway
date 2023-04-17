package com.xuanluan.mc.cloud.controller;

import com.xuanluan.mc.cloud.service.SwaggerService;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Xuan Luan
 * @createdAt 1/14/2023
 */
@RestController
public class SwaggerController {

    private final SwaggerService swaggerService;

    public SwaggerController(SwaggerService swaggerService) {
        this.swaggerService = swaggerService;
    }

    @GetMapping("swagger/{service}")
    public Mono<Void> redirectSwaggerServiceUI(@PathVariable String service,
                                               @RequestHeader(name = "X-CSRFToken") String token,
                                               ServerHttpResponse response) {
        String suffix = "/swagger-ui/index.html#/";
        String location = "/" + service + "-service" + suffix;
        return swaggerService.redirectSwaggerUI(token, response, location);
    }
}
