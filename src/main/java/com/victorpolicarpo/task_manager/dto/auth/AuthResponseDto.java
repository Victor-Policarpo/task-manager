package com.victorpolicarpo.task_manager.dto.auth;

import com.victorpolicarpo.task_manager.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Authentication response DTO")
public class AuthResponseDto {
    @Schema(description = "User ID", example = "1")
    private Long id;
    @Schema(description = "User name", example = "Luanna Veloso")
    private String name;
    @Schema(description = "User age", example = "25")
    private Integer age;
    @Schema(description = "User email", example = "user@example.com")
    private String email;
    @Schema(description = "User role", example = "USER")
    private Role role;
    @Schema(description = "Authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
