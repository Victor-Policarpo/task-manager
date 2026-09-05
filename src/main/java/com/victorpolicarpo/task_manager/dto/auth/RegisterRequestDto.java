package com.victorpolicarpo.task_manager.dto.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for user registration request.")
public class RegisterRequestDto {

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Size(min = 3, max = 50, message = "This field must contain 3 to 50 characters.")
    @Schema(description = "The name of the user.", example = "Luana Veloso")
    private String name;

    @NotNull(message = "This field cannot be null.")
    @Min(value = 10, message = "Users must be at least 10 years old.")
    @Schema(description = "The age of the user.", example = "25")
    private Integer age;

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Email(message = "Invalid email format.")
    @Schema(description = "The email address of the user.", example = "user@example.com")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).*$",
            message = "Your password must contain at least one special character, one number, and one letter."
    )
    @Size(min = 8, max = 16, message = "This field must contain 8 to 16 characters.")
    @Schema(description = "The password of the user.", example = "Password123!")
    private String password;

    @Operation(summary = "Get the email address in lowercase.", description = "Returns the email address in lowercase format.")
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
