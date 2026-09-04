package com.victorpolicarpo.task_manager.security;

import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.model.Role;
import com.victorpolicarpo.task_manager.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserTest {

    @Mock
    private UserRepository userRepository;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private User user() {
        User user = new User();
        user.setId(1L);
        user.setName("Victor");
        user.setAge(25);
        user.setEmail("victor@example.com");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        return user;
    }

    @Test
    void getShouldReturnAuthenticatedUser() {
        User user = user();
        when(userRepository.findByEmail("victor@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "victor@example.com", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);
        User result = authenticatedUser.get();

        assertEquals(1L, result.getId());
        assertEquals("victor@example.com", result.getEmail());
    }

    @Test
    void getIdShouldReturnAuthenticatedUserId() {
        User user = user();
        when(userRepository.findByEmail("victor@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "victor@example.com", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);
        Long id = authenticatedUser.getId();

        assertEquals(1L, id);
    }

    @Test
    void getShouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "unknown@example.com", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);
        assertThrows(BadCredentialsException.class, authenticatedUser::get);
    }

    @Test
    void getShouldThrowWhenNoAuthentication() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);
        assertThrows(Exception.class, authenticatedUser::get);
    }
}
