package com.victorpolicarpo.task_manager.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Represents a validation error for a specific field.")
public class FieldMessage {
    @Schema(description = "The name of the field that caused the validation error.", example = "username")
    private String fieldName;
    @Schema(description = "The validation error message associated with the field.", example = "Username must not be empty.")
    private String message;
}
