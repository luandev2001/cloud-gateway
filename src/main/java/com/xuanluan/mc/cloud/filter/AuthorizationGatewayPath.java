package com.xuanluan.mc.cloud.filter;

import com.xuanluan.mc.auth.model.enums.RoleAccount;
import com.xuanluan.mc.exception.ServiceNotStackTraceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author Xuan Luan
 * @createdAt 11/12/2022
 */
public class AuthorizationGatewayPath {

    public static boolean isCheckPermissionCorrect(RoleAccount role, ServerHttpRequest request) {
        if (AuthorizationGatewayRequest.requiredLogin().test(request)) {
            return true;
        }

        switch (role) {
            case CUSTOMER:
                return AuthorizationGatewayRequest.apisRequiredCustomer().test(request);
            case SUPER_ADMIN:
                return AuthorizationGatewayRequest.apisRequiredSuperAdmin().test(request);
            case EMPLOYEE:
                return AuthorizationGatewayRequest.apisRequiredEmployee().test(request);
            case ADMIN:
                return AuthorizationGatewayRequest.apisRequiredAdmin().test(request);
            case DEVELOPER:
                return AuthorizationGatewayRequest.apisRequiredDeveloper().test(request);
            default:
                return false;
        }
    }

    public static void validatePermission() throws ServiceNotStackTraceException {
        throw new ServiceNotStackTraceException(
                HttpStatus.FORBIDDEN,
                "You do not have access to this function!",
                "Bạn không có quyền truy cập đến tài liệu này!"
        );
    }
}
