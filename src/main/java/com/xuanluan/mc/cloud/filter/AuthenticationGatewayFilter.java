package com.xuanluan.mc.cloud.filter;

import com.xuanluan.mc.auth.model.enums.RoleAccount;
import com.xuanluan.mc.cloud.jwt.JwtProvider;
import com.xuanluan.mc.exception.ServiceNotStackTraceException;
import com.xuanluan.mc.utils.BaseStringUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Xuan Luan
 * @createdAt 10/9/2022
 */
@RequiredArgsConstructor
@Component
public class AuthenticationGatewayFilter implements GatewayFilter {

    @Value("${session.token.header}")
    private String tokenHeader;

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        try {
            if (AuthorizationGatewayRequest.requiredLogin().test(request) || AuthorizationGatewayRequest.notRequiredLogin().test(request)) {
                if (!request.getHeaders().containsKey(tokenHeader)) {
                    throw new ServiceNotStackTraceException(HttpStatus.UNAUTHORIZED, "Please login to your account!", "Xin vui lòng đăng nhập đến tài khoản!");
                }

                String clientId = request.getHeaders().getFirst("clientId");
                if (!BaseStringUtils.hasTextAfterTrim(clientId)) {
                    throw new ServiceNotStackTraceException(HttpStatus.BAD_REQUEST, "clientId header is null", "clientId header bị rỗng");
                }
                String token = jwtProvider.resolveToken(request, tokenHeader);
                validateToken(token);

                if (jwtProvider.validateToken(token)) {
                    Claims claims = jwtProvider.getClaims(token);
                    //check role type of user
                    List<String> roles = (List<String>) claims.get("roles");
                    Assert.isTrue(roles != null && roles.size() > 0, "roles cannot be null or empty");

                    boolean isSuccessful = false;
                    for (String role : roles) {
                        RoleAccount roleAccount = getRoleAccount(role);
                        if (roleAccount != null && AuthorizationGatewayPath.isCheckPermissionCorrect(roleAccount, request)) {
                            isSuccessful = true;
                            break;
                        }
                    }

                    if (!isSuccessful) AuthorizationGatewayPath.validatePermission();

                    //set header to another client use
                    exchange.getRequest().mutate()
                            .header("clientId", clientId)
                            .build();
                }
            }
        } catch (ServiceNotStackTraceException e) {
            return Mono.error(e);
        }

        return chain.filter(exchange);
    }


    private RoleAccount getRoleAccount(String role) {
        try {
            return RoleAccount.valueOf(role);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void validateToken(String token) throws ServiceNotStackTraceException {
        if (!BaseStringUtils.hasTextAfterTrim(token)) {
            throw new ServiceNotStackTraceException(HttpStatus.UNAUTHORIZED, "Invalid token information!", "Mã phiên đăng nhập không hợp lệ hoặc đã hết hạn!");
        }
    }
}
