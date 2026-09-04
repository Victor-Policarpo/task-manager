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

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto dto) {
        User user = authenticatedUser.get();
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setCompleted(false);
        task.setUser(user);
        Task saved = repository.save(task);
        return taskMapper.toResponseDto(saved);
    }

    public List<TaskResponseDto> listAll() {
        Long userId = authenticatedUser.getId();
        List<Task> tasks = repository.findAllByUserIdOrderByIdAsc(userId);
        return taskMapper.toResponseDtoList(tasks);
    }

    public TaskResponseDto findById(Long id) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        return taskMapper.toResponseDto(task);
    }

    @Transactional
    public TaskResponseDto taskCompleted(Long id) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        task.setCompleted(true);
        Task saved = repository.save(task);
        return taskMapper.toResponseDto(saved);
    }

    public List<TaskResponseDto> filterByStatus(boolean completed) {
        Long userId = authenticatedUser.getId();
        List<Task> tasks = repository.findAllByUserIdAndCompletedOrderByIdAsc(userId, completed);
        return taskMapper.toResponseDtoList(tasks);
    }

    @Transactional
    public void delete(Long id) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        repository.delete(task);
    }

    @Transactional
    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        Long userId = authenticatedUser.getId();
        Task task = findOwnedTask(id, userId);
        taskMapper.updateEntityFromDto(dto, task);
        Task saved = repository.save(task);
        return taskMapper.toResponseDto(saved);
    }

    private Task findOwnedTask(Long taskId, Long userId) {
        return repository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }
}
