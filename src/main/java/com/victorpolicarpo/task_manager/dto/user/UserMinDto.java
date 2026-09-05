package com.victorpolicarpo.task_manager.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "UserMinDto is a data transfer object that represents a minimal view of a user, containing only the user's ID and name.")
public class UserMinDto {
    @Schema(description = "The unique identifier of the user.", example = "1")
    private Long id;
    @Schema(description = "The name of the user.", example = "John Doe")
    private String name;
}