package com.victorpolicarpo.task_manager.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Schema(description = "UserRequestDto is a data transfer object that represents the request body for creating or updating a user, containing the user's name, age, email, and password.")
public class UserRequestDto {

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Size(min = 3, max = 50, message = "This field must contain 3 to 50 characters.")
    @Schema(description = "The name of the user.", example = "John Doe")
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
    @Schema(description = "The password of the user.", example = "P@ssw0rd123")
    private String password;



}
