package com.victorpolicarpo.task_manager.dto.user;

import com.victorpolicarpo.task_manager.dto.task.TaskMinDto;
import com.victorpolicarpo.task_manager.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserResponseDto is a data transfer object that represents the response body for retrieving user information, containing the user's ID, name, age, email, role, and a list of tasks associated with the user.")
public class UserResponseDto {
    @Schema(description = "The unique identifier of the user.", example = "1")
    private Long id;
    @Schema(description = "The name of the user.", example = "John Doe")
    private String name;
    @Schema(description = "The age of the user.", example = "25")
    private Integer age;
    @Schema(description = "The email address of the user.", example = "user@example.com")
    private String email;
    @Schema(description = "The role of the user.", example = "ADMIN")
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "A list DTOs of tasks associated with the user.")
    private List<TaskMinDto> tasks;
}
