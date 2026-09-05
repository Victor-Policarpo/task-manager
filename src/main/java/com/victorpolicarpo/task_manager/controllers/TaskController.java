package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.task.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.task.TaskUpdateDto;
import com.victorpolicarpo.task_manager.exception.StandardError;
import com.victorpolicarpo.task_manager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task Management", description = "Endpoints for managing tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(
            summary = "List all tasks",
            description = "Retrieves a list of all tasks.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
                @ApiResponse(responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    public ResponseEntity<List<TaskResponseDto>> listAll(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.listAll());
    }

    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details.",
            responses = {
                @ApiResponse(responseCode = "201", description = "Task created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request data",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(responseCode = "401", description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(responseCode = "403", description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequestDto));
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a task by its ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    public ResponseEntity<TaskResponseDto> findById(@Parameter(description = "ID of the task to retrieve") @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
    }

    @PatchMapping("/{id}/complete")
    @Operation(
            summary = "Mark task as completed",
            description = "Marks a task as completed by its ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Task marked as completed successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    public ResponseEntity<TaskResponseDto> taskCompleted(@Parameter(description = "ID of the task to complete") @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.taskCompleted(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Filter tasks by completion status",
            description = "Filters tasks based on their completion status.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Tasks filtered successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tasks not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    public ResponseEntity<List<TaskResponseDto>> filterByStatus(@Parameter(description = "Indicates if the tasks should be completed") @RequestParam boolean completed){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.filterByStatus(completed));
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its ID.",
            responses = {
                @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    }
    )
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            schema = @Schema(implementation = StandardError.class
                            ))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            schema = @Schema(implementation = StandardError.class
                            ))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden access",
                    content = @Content(
                            schema = @Schema(implementation = StandardError.class
                            ))
            )
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the task to delete") @PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update a task",
            description = "Updates a task by its ID with the provided details.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Task updated successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request data",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden access",
                        content = @Content(
                                schema = @Schema(implementation = StandardError.class
                                ))
                )
    })
    public ResponseEntity<TaskResponseDto> update(@Parameter(description = "ID of the task to update") @PathVariable Long id,@Valid @RequestBody TaskUpdateDto taskUpdateDto){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(id, taskUpdateDto));
    }
}
