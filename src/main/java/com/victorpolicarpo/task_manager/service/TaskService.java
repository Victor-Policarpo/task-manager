package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.task.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.task.TaskUpdateDto;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.TaskMapper;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import com.victorpolicarpo.task_manager.security.AuthenticatedUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper taskMapper;
    private final AuthenticatedUser authenticatedUser;
    private final TaskImageService taskImageService;

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto dto) {
        User user = authenticatedUser.get();
        return taskMapper.toResponseDto(repository.save(taskMapper.toEntity(dto, user)));
    }

    public List<TaskResponseDto> listAll() {
        Long userId = authenticatedUser.getId();
        List<Task> tasks = repository.findAllByUserIdOrderByIdAsc(userId);
        return tasks.stream()
                .map(this::toResponseDtoWithImageUrls)
                .toList();
    }

    public TaskResponseDto findById(Long id) {
        Long userId = authenticatedUser.getId();
        return toResponseDtoWithImageUrls(findOwnedTask(id, userId));
    }

    @Transactional
    public TaskResponseDto taskCompleted(Long id) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        task.setCompleted(true);
        return toResponseDtoWithImageUrls(repository.save(task));
    }

    public List<TaskResponseDto> filterByStatus(boolean completed) {
        Long userId = authenticatedUser.getId();
        List<Task> tasks = repository.findAllByUserIdAndCompletedOrderByIdAsc(userId, completed);
        return tasks.stream()
                .map(this::toResponseDtoWithImageUrls)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        taskImageService.deleteImagesFromStorage(task.getImages());
        repository.delete(task);
    }

    @Transactional
    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        taskMapper.updateEntityFromDto(dto, task);
        return toResponseDtoWithImageUrls(repository.save(task));
    }

    private Task findOwnedTask(Long taskId, Long userId) {
        return repository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private TaskResponseDto toResponseDtoWithImageUrls(Task task) {
        TaskResponseDto dto = taskMapper.toResponseDto(task);
        dto.setImages(
                taskImageService.toResponseDtoListWithPresignedUrls(task.getImages())
        );

        return dto;
    }

}
