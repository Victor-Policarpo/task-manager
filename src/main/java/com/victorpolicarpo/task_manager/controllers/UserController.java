package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.exception.StandardError;
import com.victorpolicarpo.task_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management API")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Returns the profile of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User does not have permission to access this resource",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
            }
    )
    public ResponseEntity<UserResponseDto> getProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile());
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Update current user profile",
            description = "Updates the profile of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated user profile"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid input data",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User does not have permission to access this resource",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
            }
    )
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userUpdateDto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns the user with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User does not have permission to access this resource",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
            }
    )
    public ResponseEntity<UserResponseDto> findById(@Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @Operation(
            summary = "List all users",
            description = "Returns a list of all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User does not have permission to access this resource",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.listAll());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes the user with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User does not have permission to access this resource",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    schema = @Schema(implementation = StandardError.class
                                    ))
                    ),
            }
    )
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
