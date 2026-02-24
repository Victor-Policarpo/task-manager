package com.victorpolicarpo.task_manager.dto.task;

import com.victorpolicarpo.task_manager.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskRequestDto {
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 50, message = "The field must contain between 3 and 50 characters.")
    private String title;
    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 250, message = "The field must contain between 3 and 250 characters.")
    private String content;
    @NotNull(message = "user_id must not be null")
    private Long user_id;
}
