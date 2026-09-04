package com.victorpolicarpo.task_manager.security;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void validTokenShouldAuthenticateRequest() throws Exception {
        UserDetails userDetails = new User(
                "victor@example.com",
                "hash",
                java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        MockHttpServletRequest request = requestWithBearerToken("valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        when(jwtService.extractUsername("valid-token")).thenReturn("victor@example.com");
        when(userDetailsService.loadUserByUsername("victor@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-token", userDetails)).thenReturn(true);
        doAnswer(invocation -> {
            assertEquals("victor@example.com", SecurityContextHolder.getContext().getAuthentication().getName());
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void invalidTokenShouldNotAuthenticateRequest() throws Exception {
        MockHttpServletRequest request = requestWithBearerToken("invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        when(jwtService.extractUsername("invalid-token")).thenThrow(new MalformedJwtException("Invalid token"));
        doAnswer(invocation -> {
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    private MockHttpServletRequest requestWithBearerToken(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
