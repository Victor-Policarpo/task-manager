package com.victorpolicarpo.task_manager.dto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDto {

    @Size(min = 3, max = 50, message = "The field must contain between 3 and 50 characters.")
    private String title;

    @Size(min = 3, max = 250, message = "The field must contain between 3 and 250 characters.")
    private String content;
}
