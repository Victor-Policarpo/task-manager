package com.victorpolicarpo.task_manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorpolicarpo.task_manager.exception.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError("Unauthorized");
        error.setMessage("Authentication is required to access this resource.");
        error.setPath(request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
