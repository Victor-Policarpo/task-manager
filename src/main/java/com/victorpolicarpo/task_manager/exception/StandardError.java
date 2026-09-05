package com.victorpolicarpo.task_manager.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Standard error response for the application.")
public class StandardError implements Serializable {
    @Schema(description = "Timestamp of the error occurrence.", example = "2024-06-01T12:00:00Z")
    private Instant timestamp;
    @Schema(description = "HTTP status code of the error.", example = "404")
    private Integer status;
    @Schema(description = "Error type or description.", example = "Resource not found")
    private String error;
    @Schema(description = "Detailed error message.", example = "The requested resource was not found.")
    private String message;
    @Schema(description = "Path of the request that caused the error.", example = "/api/tasks/1")
    private String path;
}
