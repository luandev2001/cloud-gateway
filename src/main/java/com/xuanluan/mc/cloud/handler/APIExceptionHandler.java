package com.xuanluan.mc.cloud.handler;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Xuan Luan
 * @createdAt 10/9/2022
 */
@Component
public class APIExceptionHandler extends AbstractErrorWebExceptionHandler {

    public APIExceptionHandler(ErrorAttributes errorAttributes,
                               WebProperties.Resources resources,
                               ApplicationContext applicationContext,
                               ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }

    private Mono<ServerResponse> renderException(ServerRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        Map<String, Object> error = getErrorAttributes(request, options);
        error.remove("requestId");
        error.remove("path");

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(error));
    }
}
