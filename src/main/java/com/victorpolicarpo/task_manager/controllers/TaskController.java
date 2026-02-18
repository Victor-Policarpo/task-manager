package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.TaskUpdateDto;
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
        return ResponseEntity.ok(taskService.listAll());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto){
        return new ResponseEntity<>(taskService.createTask(taskRequestDto), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDto> taskCompleted(@PathVariable Long id){
        return ResponseEntity.ok(taskService.taskCompleted(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> filterByStatus(@RequestParam boolean completed){
        return ResponseEntity.ok(taskService.filterByStatus(completed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Long id,@Valid @RequestBody TaskUpdateDto taskUpdateDto){
        return ResponseEntity.ok(taskService.update(id, taskUpdateDto));
    }
}
