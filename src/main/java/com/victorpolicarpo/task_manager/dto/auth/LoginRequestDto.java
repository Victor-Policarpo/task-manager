package com.victorpolicarpo.task_manager.dto.auth;

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
public class LoginRequestDto {

    @NotBlank(message = "This field is required and cannot be left blank.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "This field is required and cannot be left blank.")
    private String password;

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
