package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.TaskUpdateDto;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.TaskMapper;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    public final TaskRepository repository;
    public final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = taskMapper.toEntity(taskRequestDto);
        Task taskSaved = repository.save(task);
        return taskMapper.toResponseDto(taskSaved);
    }


    public List<TaskResponseDto> listAll() {
        List<Task> taskList = repository.findAll(Sort.by("id").ascending());
       return taskMapper.toResponseDtoList(taskList);
    }


    public TaskResponseDto findById(Long id) {
        Task task = findEntityById(id);
        return taskMapper.toResponseDto(task);

    }

    @Transactional
    public TaskResponseDto taskCompleted(Long id) {
        Task task = findEntityById(id);
        task.setCompleted(true);
        Task taskSaved = repository.save(task);
        return taskMapper.toResponseDto(taskSaved);
    }

    public List<TaskResponseDto> filterByStatus(boolean completed) {
        List<Task> tasks = repository.findByCompleted(completed);
        return taskMapper.toResponseDtoList(tasks);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findEntityById(id));
    }

    @Transactional
    public TaskResponseDto update(Long id, TaskUpdateDto taskUpdateDto) {
        Task task = findEntityById(id);
        taskMapper.updateEntityFromDto(taskUpdateDto, task);
        Task taskSaved = repository.save(task);
        return taskMapper.toResponseDto(taskSaved);
    }

    public Task findEntityById(Long id) {
        return repository.findById(id).
                orElseThrow(
                        () -> new ResourceNotFoundException("Task Id not found or not exist")
                );
    }

}
