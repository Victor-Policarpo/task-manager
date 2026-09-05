package com.victorpolicarpo.task_manager.dto.task;
import com.victorpolicarpo.task_manager.dto.user.UserMinDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for task response.")
public class TaskResponseDto {
    @Schema(description = "The unique identifier of the task.", example = "1")
    private Long id;
    @Schema(description = "The title of the task.", example = "Buy groceries")
    private String title;
    @Schema(description = "The content of the task.", example = "Buy milk, bread, and eggs.")
    private String content;
    @Schema(description = "The completion status of the task.", example = "false")
    private Boolean completed;
    @Schema(description = "The user DTO associated with the task.")
    private UserMinDto user;
}
