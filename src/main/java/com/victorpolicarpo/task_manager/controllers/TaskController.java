package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.task.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.task.TaskUpdateDto;
import com.victorpolicarpo.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> listAll(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.listAll());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequestDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDto> taskCompleted(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.taskCompleted(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> filterByStatus(@RequestParam boolean completed){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.filterByStatus(completed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Long id,@Valid @RequestBody TaskUpdateDto taskUpdateDto){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(id, taskUpdateDto));
    }
}
