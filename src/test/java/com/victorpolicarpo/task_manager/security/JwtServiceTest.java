package com.victorpolicarpo.task_manager.security;

import com.victorpolicarpo.task_manager.model.Role;
import com.victorpolicarpo.task_manager.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    @Test
    void generateTokenShouldContainExpectedClaimsAndExpiration() {
        JwtService jwtService = jwtServiceWithExpiration(60_000);
        User user = new User(1L, "Victor", 25, "victor@example.com", "$2a$10$hash", Role.USER, null);

        String token = jwtService.generateToken(user);

        assertEquals("victor@example.com", jwtService.extractUsername(token));
        assertEquals(1L, jwtService.extractAllClaims(token).get("uid", Long.class));
        assertEquals(java.util.List.of("USER"), jwtService.extractAllClaims(token).get("roles", java.util.List.class));
        assertNotNull(jwtService.extractAllClaims(token).getIssuedAt());
        assertNotNull(jwtService.extractAllClaims(token).getExpiration());
    }

    @Test
    void expiredTokenShouldNotBeAccepted() {
        JwtService jwtService = jwtServiceWithExpiration(-1);
        User user = new User(1L, "Victor", 25, "victor@example.com", "$2a$10$hash", Role.USER, null);

        String expiredToken = jwtService.generateToken(user);

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
    }

    private JwtService jwtServiceWithExpiration(long expiration) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8)
        ));
        properties.setExpiration(expiration);
        JwtService jwtService = new JwtService(properties);
        jwtService.initializeSigningKey();
        return jwtService;
    }
}
