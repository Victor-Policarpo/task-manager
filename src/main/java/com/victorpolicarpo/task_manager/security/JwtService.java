package com.victorpolicarpo.task_manager.security;

import com.victorpolicarpo.task_manager.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private SecretKey signingKey;

    @PostConstruct
    void initializeSigningKey() {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public String generateToken(User user) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .claim("roles", List.of(user.getRole().name()))
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
