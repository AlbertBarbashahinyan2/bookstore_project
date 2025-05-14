package com.example.demospring1.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    public static final String AUTH_TYPE = "Bearer ";
    public static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60; // 1 hour
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30; // 30 days
    private static final String ROLES = "roles";

    @Value("${jwt.secret}")
    private String secret;

    public String getUsername(String token) {
        return this.verifyAndDecode(token).getSubject();
    }

    private DecodedJWT verifyAndDecode(String token) {
        return JWT.require(this.getAlgorithm()).build().verify(token);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        String jti = UUID.randomUUID().toString();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withJWTId(jti)
                .withClaim(ROLES, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(this.getAlgorithm());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        String jti = UUID.randomUUID().toString();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .withJWTId(jti)
                .sign(this.getAlgorithm());
    }

    public boolean isVerified(String token) {
        try {
            this.verifyAndDecode(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getJti(String token) {
        return this.verifyAndDecode(token).getId();
    }

    public String[] getAuthorities(String token) {
        return this.verifyAndDecode(token).getClaim(ROLES).asArray(String.class);
    }
}