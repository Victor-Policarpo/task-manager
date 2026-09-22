package com.victorpolicarpo.task_manager.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for task creation request.")
public class TaskRequestDto {
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 255, message = "The field must contain between 3 and 255 characters.")
    @Schema(description = "The title of the task.", example = "Buy groceries")
    private String title;
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 5000, message = "The field must contain between 3 and 5000 characters.")
    @Schema(description = "The content of the task.", example = "Buy milk, bread, and eggs.")
    private String content;
    @Schema(description = "The images of the task.")
    List<TaskResponseDto> images;
}
