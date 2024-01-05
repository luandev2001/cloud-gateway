package com.xuanluan.mc.cloud.jwt;

import com.xuanluan.mc.sdk.exception.ServiceNotStackTraceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

/**
 * @author Xuan Luan
 * @createdAt 10/9/2022
 */
@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Claims getClaims(String token) throws ServiceNotStackTraceException {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new ServiceNotStackTraceException(
                    HttpStatus.UNAUTHORIZED,
                    "JWT token expired or invalid!",
                    "JWT token đã hết hạn hoặc không hợp lệ!");
        }
    }

    public boolean validateToken(String token) throws ServiceNotStackTraceException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new ServiceNotStackTraceException(
                    HttpStatus.UNAUTHORIZED,
                    "JWT token expired or invalid!",
                    "JWT token đã hết hạn hoặc không hợp lệ!");
        }
    }

    public String resolveToken(ServerHttpRequest request,String header) {
        return request.getHeaders().getOrEmpty(header).get(0);
    }
}
