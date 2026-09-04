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

    @Test
    void getShouldReturnAuthenticatedUser() {
        User user = new User(1L, "Victor", 25, "victor@example.com", "hash", Role.USER, null);
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
        User user = new User(1L, "Victor", 25, "victor@example.com", "hash", Role.USER, null);
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
