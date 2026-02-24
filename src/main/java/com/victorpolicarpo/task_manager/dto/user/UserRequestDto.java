package com.victorpolicarpo.task_manager.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserRequestDto {

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Size(min = 3, max = 50, message = "This field must contain 3 to 50 characters.")
    private String name;
    @NotNull(message = "This field cannot be null.")
    @Min(value = 10, message = "Users must be at least 10 years old.")
    private Integer age;
    @NotBlank(message = "This field is required and cannot be left blank.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).*$",
            message = "Your password must contain at least one special character, one number, and one letter."
    )
    @Size(min = 8, max = 16, message = "This field must contain 8 to 16 characters.")
    private String password;



}
