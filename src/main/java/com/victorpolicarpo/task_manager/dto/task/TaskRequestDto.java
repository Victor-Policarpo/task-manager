package com.victorpolicarpo.task_manager.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for task creation request.")
public class TaskRequestDto {
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 50, message = "The field must contain between 3 and 50 characters.")
    @Schema(description = "The title of the task.", example = "Buy groceries")
    private String title;
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 250, message = "The field must contain between 3 and 250 characters.")
    @Schema(description = "The content of the task.", example = "Buy milk, bread, and eggs.")
    private String content;
}
