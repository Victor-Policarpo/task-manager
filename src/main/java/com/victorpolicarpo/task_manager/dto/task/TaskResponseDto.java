package com.victorpolicarpo.task_manager.dto.task;
import com.victorpolicarpo.task_manager.dto.user.UserMinDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String content;
    private Boolean completed;
    private UserMinDto user;
}
