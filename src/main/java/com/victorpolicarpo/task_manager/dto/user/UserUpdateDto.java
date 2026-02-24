package com.victorpolicarpo.task_manager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Size(min = 3, max = 50, message = "This field must contain 3 to 50 characters.")
    @Pattern(regexp = ".*\\S.*", message = "Name cannot be blank.")
    private String name;

    @Min(value = 10, message = "Users must be at least 10 years old.")
    private Integer age;

    @Email(message = "Invalid email format.")
    private String email;


}
