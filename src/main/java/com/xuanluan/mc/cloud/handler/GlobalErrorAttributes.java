package com.xuanluan.mc.cloud.handler;

import com.xuanluan.mc.exception.ServiceNotStackTraceException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xuan Luan
 * @createdAt 10/9/2022
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<>();
        try {
            ServiceNotStackTraceException error = (ServiceNotStackTraceException) getError(request);
            errorMap.put("message", error.getErrorMessage());
            errorMap.put("data", error.getErrorDetail());
            errorMap.put("status", error.getErrorCode());
        } catch (ClassCastException e) {
            Throwable error = getError(request);
            errorMap.put("message", error.getMessage());
            errorMap.put("data", errorServer(error.getMessage()) + ", xin vui lòng kiểm tra lại server!");
            errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return errorMap;
    }

    private String errorServer(String message) {
        if (message == null) {
            return "Server đã xảy ra lỗi";
        }
        String[] splitRegex = message.split(":");
        if (splitRegex.length < 4) {
            return "Server đã xảy ra lỗi";
        }
        String host = splitRegex[3];
        switch (host) {
            case "8081":
                return "ADMIN-SERVICE không phản hồi";
            case "8082":
                return "AUTH-SERVICE không phản hồi";
            case "8083":
                return "CLIENT-SERVICE không phản hồi";
            case "8084":
                return "ORGANIZATION-SERVICE không phản hồi";
            case "8085":
                return "PRODUCT-SERVICE không phản hồi";
        }
        return "Server đã xảy ra lỗi";
    }
}
