package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.taskImage.TaskImageResponseDto;
import com.victorpolicarpo.task_manager.service.TaskImageService;
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
public class TaskImageController {
    private final TaskImageService taskImageService;


    @PostMapping("/{id}/images")
    public ResponseEntity<Void> uploadImages(@RequestParam("file") List<MultipartFile> file, @PathVariable Long id) throws IOException {
        taskImageService.uploadImages(file, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<TaskImageResponseDto>> getTaskImages(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskImageService.getTaskImages(id));
    }

    @DeleteMapping("/{taskId}/images/{imageId}")
    public ResponseEntity<Void> deleteTaskImage(@PathVariable Long taskId, @PathVariable Long imageId){
        taskImageService.deleteTaskImage(taskId, imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
