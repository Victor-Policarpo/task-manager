package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
