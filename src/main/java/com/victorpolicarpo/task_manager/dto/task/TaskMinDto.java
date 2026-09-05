package com.victorpolicarpo.task_manager.dto.task;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data Transfer Object for minimal task information.")
public class TaskMinDto {
    @Schema(description = "The unique identifier of the task.", example = "1")
    private Long id;
    @Schema(description = "The title of the task.", example = "Buy groceries")
    private String title;

}
