package com.victorpolicarpo.task_manager.dto.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for user login request.")
public class LoginRequestDto {

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Email(message = "Invalid email format.")
    @Schema(
            description = "The email address of the user.",
            example = "user@email.com"
    )
    private String email;

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Schema(
            description = "The password of the user.",
            example = "password123"
    )
    private String password;

    @Operation(summary = "Get the email address in lowercase.", description = "Returns the email address in lowercase format.")
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
