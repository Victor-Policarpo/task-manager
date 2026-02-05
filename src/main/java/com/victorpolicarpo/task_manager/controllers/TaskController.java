package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> listAll(){
        return ResponseEntity.ok(taskService.listAll());
    }

    @PostMapping("/create-task")
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> taskCompleted(@PathVariable Long id){
        return ResponseEntity.ok(taskService.taskCompleted(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filterByStatus(@RequestParam boolean completed){
        return ResponseEntity.ok(taskService.filterByStatus(completed));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
