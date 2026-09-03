package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.auth.AuthResponseDto;
import com.victorpolicarpo.task_manager.dto.auth.LoginRequestDto;
import com.victorpolicarpo.task_manager.dto.auth.RegisterRequestDto;
import com.victorpolicarpo.task_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
}
