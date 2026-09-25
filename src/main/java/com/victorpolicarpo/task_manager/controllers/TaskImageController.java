package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.taskImage.TaskImageResponseDto;
import com.victorpolicarpo.task_manager.exception.StandardError;
import com.victorpolicarpo.task_manager.service.TaskImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Images", description = "Endpoints for managing task images")
public class TaskImageController {
    private final TaskImageService taskImageService;

    @Operation(
            summary = "Upload images for a task",
            description = """
                            Uploads one or more images for the specified task.
                            Supported formats: JPEG, PNG and WEBP.
                            Maximum of 5 images per task.
                            Maximum file size: 5 MB per image.
                            """,
            responses = {
                @ApiResponse(responseCode = "201", description = "Images uploaded successfully"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request data, file format or size",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                )
            }
    )
    @PostMapping("/{id}/images")
    public ResponseEntity<Void> uploadImages(@Parameter(
            description = "One or more images to upload. Supported formats: JPEG, PNG and WEBP.",
            required = true
    ) @RequestParam("files") List<MultipartFile> file, @Parameter(
            description = "ID of the task",
            example = "1",
            required = true
    ) @PathVariable Long id) throws IOException {
        taskImageService.uploadImages(file, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get images for a task",
            description = "Retrieves all images associated with the specified task.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                )
            }
    )
    @GetMapping("/{id}/images")
    public ResponseEntity<List<TaskImageResponseDto>> getTaskImages(@Parameter(
            description = "ID of the task",
            example = "1",
            required = true
    ) @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskImageService.getTaskImages(id));
    }

    @Operation(
            summary = "Delete an image for a task",
            description = "Deletes the specified image associated with the given task.",
            responses = {
                @ApiResponse(responseCode = "204", description = "Image deleted successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task or image not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class)
                        )
                )
            }
    )
    @DeleteMapping("/{taskId}/images/{imageId}")
    public ResponseEntity<Void> deleteTaskImage(@Parameter(
            description = "ID of the task",
            example = "1",
            required = true
    ) @PathVariable Long taskId, @Parameter(
            description = "ID of the image to delete",
            example = "7",
            required = true
    ) @PathVariable Long imageId){
        taskImageService.deleteTaskImage(taskId, imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
