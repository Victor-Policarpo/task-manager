package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    public final TaskRepository repository;

    @Transactional
    public Task createTask(Task task) {
        return repository.save(task);
    }

    public List<Task> listAll() {
       return repository.findAll();
    }

    public Task findById(Long id) {
        return repository.findById(id).
                orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task Id not found our not exist")
        );

    }
}
