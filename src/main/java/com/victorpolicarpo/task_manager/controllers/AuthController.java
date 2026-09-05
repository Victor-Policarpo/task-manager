package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.auth.AuthResponseDto;
import com.victorpolicarpo.task_manager.dto.auth.LoginRequestDto;
import com.victorpolicarpo.task_manager.dto.auth.RegisterRequestDto;
import com.victorpolicarpo.task_manager.exception.StandardError;
import com.victorpolicarpo.task_manager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided registration details.",
            responses = {
                @ApiResponse(responseCode = "201", description = "User registered successfully"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request data",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))),
                @ApiResponse(
                        responseCode = "409",
                        description = "Email already exists",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequestDto));
    }

    @Operation(
            summary = "Login a user",
            description = "Authenticates a user with the provided login details.",
            responses = {
                @ApiResponse(responseCode = "200", description = "User logged in successfully"),
                @ApiResponse(responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request data",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequestDto));
    }
}
