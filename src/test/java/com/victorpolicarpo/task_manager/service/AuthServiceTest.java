package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.auth.AuthResponseDto;
import com.victorpolicarpo.task_manager.dto.auth.LoginRequestDto;
import com.victorpolicarpo.task_manager.dto.auth.RegisterRequestDto;
import com.victorpolicarpo.task_manager.exception.ConflictException;
import com.victorpolicarpo.task_manager.mapper.UserMapper;
import com.victorpolicarpo.task_manager.model.Role;
import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.repository.UserRepository;
import com.victorpolicarpo.task_manager.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager, jwtService, userMapper);
    }

    @Test
    void registerShouldHashPasswordNormalizeEmailAndAssignUserRole() {
        RegisterRequestDto request = new RegisterRequestDto(
                "Victor", 25, "  VICTOR@EXAMPLE.COM  ", "Secure1!"
        );
        when(userMapper.toEntity(any(RegisterRequestDto.class))).thenAnswer(invocation -> {
            RegisterRequestDto dto = invocation.getArgument(0);
            User user = new User();
            user.setName(dto.getName());
            user.setAge(dto.getAge());
            return user;
        });
        when(userRepository.existsByEmail("victor@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        AuthResponseDto response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("victor@example.com", savedUser.getEmail());
        assertNotEquals("Secure1!", savedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches("Secure1!", savedUser.getPasswordHash()));
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals(1L, response.getId());
        assertEquals("victor@example.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());
        assertEquals(null, response.getToken());
        assertFalse(hasSensitivePasswordField());
    }

    @Test
    void registerShouldRejectDuplicateEmail() {
        RegisterRequestDto request = new RegisterRequestDto(
                "Victor", 25, "victor@example.com", "Secure1!"
        );
        when(userRepository.existsByEmail("victor@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginShouldAuthenticateGenerateJwtAndNormalizeEmail() {
        LoginRequestDto request = new LoginRequestDto("  VICTOR@EXAMPLE.COM ", "Secure1!");
        User user = new User();
        user.setId(1L);
        user.setName("Victor");
        user.setAge(25);
        user.setEmail("victor@example.com");
        user.setPasswordHash("$2a$10$hash");
        user.setRole(Role.USER);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("victor@example.com", null));
        when(userRepository.findByEmail("victor@example.com")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDto response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("victor@example.com", response.getEmail());
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("victor@example.com", "Secure1!"));
    }

    @Test
    void loginShouldPropagateInvalidCredentialsWithoutExposingTheCause() {
        LoginRequestDto request = new LoginRequestDto("victor@example.com", "Wrong1!");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        verify(userRepository, never()).findByEmail(any());
    }

    private boolean hasSensitivePasswordField() {
        return Arrays.stream(AuthResponseDto.class.getDeclaredFields())
                .map(Field::getName)
                .anyMatch(fieldName -> fieldName.equals("password") || fieldName.equals("passwordHash"));
    }
}
