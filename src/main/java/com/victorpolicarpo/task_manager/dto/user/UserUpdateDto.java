package com.victorpolicarpo.task_manager.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "UserUpdateDto is a data transfer object that represents the request body for updating a user, containing the user's name, age, and email.")
public class UserUpdateDto {
    @Size(min = 3, max = 50, message = "This field must contain 3 to 50 characters.")
    @Pattern(regexp = ".*\\S.*", message = "Name cannot be blank.")
    @Schema(description = "The name of the user.", example = "John Doe")
    private String name;

    @Min(value = 10, message = "Users must be at least 10 years old.")
    @Schema(description = "The age of the user.", example = "25")
    private Integer age;

    @Email(message = "Invalid email format.")
    @Schema(description = "The email address of the user.", example = "user@example.com")
    private String email;


}
