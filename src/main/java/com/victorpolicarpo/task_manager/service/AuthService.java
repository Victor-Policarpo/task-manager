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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        String normalizedEmail = normalizeEmail(registerRequestDto.getEmail());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email already registered.");
        }

        User user = userMapper.toEntity(registerRequestDto);

        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        return toAuthResponse(savedUser, null);
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        String normalizedEmail = normalizeEmail(loginRequestDto.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, loginRequestDto.getPassword())
        );

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials."));
        return toAuthResponse(user, jwtService.generateToken(user));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private AuthResponseDto toAuthResponse(User user, String token) {
        return new AuthResponseDto(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
