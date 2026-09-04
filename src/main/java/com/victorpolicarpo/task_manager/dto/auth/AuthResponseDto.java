package com.victorpolicarpo.task_manager.dto.auth;

import com.victorpolicarpo.task_manager.model.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Role role;
    private String token;
}
