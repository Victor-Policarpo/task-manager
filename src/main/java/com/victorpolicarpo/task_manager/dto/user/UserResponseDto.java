package com.victorpolicarpo.task_manager.dto.user;

import com.victorpolicarpo.task_manager.dto.task.TaskMinDto;
import com.victorpolicarpo.task_manager.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TaskMinDto> tasks;
}
