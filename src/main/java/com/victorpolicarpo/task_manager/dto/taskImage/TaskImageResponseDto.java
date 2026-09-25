package com.victorpolicarpo.task_manager.dto.taskImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for task image response.")
public class TaskImageResponseDto {
    @Schema(description = "The unique identifier of the task image.", example = "1")
    private Long id;
    @Schema(description = "The original filename of the uploaded image.", example = "image.jpg")
    private String originalFileName;
    @Schema(description = "The content type of the uploaded image.", example = "image/jpeg")
    private String contentType;
    @Schema(description = "The size of the uploaded image in bytes.", example = "102400")
    private Long fileSize;
    @Schema(description = "The timestamp when the image was created.", example = "2024-06-01T12:00:00")
    private LocalDateTime createdAt;
    @Schema(
            description = "A temporary presigned URL used to access the image from Amazon S3.",
            example = "https://example-bucket.s3.amazonaws.com/task-images/task-1/uuid.jpg"
    )
    private String url;
}
