package com.xuanluan.mc.cloud.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Check what permission the path requires
 *
 * @author Xuan Luan
 * @createdAt 10/10/2022
 */
public class AuthorizationGatewayRequest {
    /**
     * these APIs require login
     *
     * @return true or false
     */
    public static Predicate<ServerHttpRequest> notRequiredLogin() {
//        "/swagger-ui", "/api-docs"
        final Set<String> apiEndpoints = Set.of(
                "/auth/1.0.0", "/client/1.0.0", "/org/1.0.0"
        );

        return r -> apiEndpoints.stream().noneMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs only require login
     */
    public static Predicate<ServerHttpRequest> requiredLogin() {
        final Set<String> apiEndpoints = Set.of(
                "/auth/1.0.0/change_password", "/auth/1.0.0/current_user_info"
        );

        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs require has SUPER_ADMIN role
     *
     * @return true or false
     */
    public static Predicate<ServerHttpRequest> apisRequiredSuperAdmin() {
        final Set<String> apiEndpoints = Set.of("/sa/1.0.0");

        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs require has CUSTOMER role
     *
     * @return true or false
     */
    public static Predicate<ServerHttpRequest> apisRequiredEmployee() {
        final Set<String> apiEndpoints = Set.of("/emp/1.0.0");

        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs require has CUSTOMER role
     */
    public static Predicate<ServerHttpRequest> apisRequiredCustomer() {
        final Set<String> apiEndpoints = Set.of("/customer/1.0.0");

        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs require has ADMIN role
     */
    public static Predicate<ServerHttpRequest> apisRequiredAdmin() {
        final Set<String> apiEndpoints = Set.of("/admin/1.0.0");
        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }

    /**
     * these APIs require has DEVELOPER role
     */
    public static Predicate<ServerHttpRequest> apisRequiredDeveloper() {
        final Set<String> apiEndpoints = Set.of("/dev/1.0.0", "/swagger-ui", "/api-docs");

        return r -> apiEndpoints.stream().anyMatch(uri -> r.getURI().getPath().contains(uri));
    }
}
