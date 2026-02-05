package com.victorpolicarpo.task_manager.service;

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

    @Transactional
    public Task createTask(Task task) {
        return repository.save(task);
    }

    public List<Task> listAll() {
       return repository.findAll(Sort.by("id").ascending());
    }

    public Task findById(Long id) {
        return repository.findById(id).
                orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task Id not found our not exist")
        );
    }

    @Transactional
    public Task taskCompleted(Long id) {
        Task task = findById(id);
        task.setCompleted(true);
        return repository.save(task);
    }

    public List<Task> filterByStatus(boolean completed) {
        return repository.findByCompleted(completed);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Transactional
    public Task update(Long id, Task updatedTask) {
        Task task = findById(id);
        if (updatedTask.getTitle() != null) {
            task.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getContent() != null) {
            task.setContent(updatedTask.getContent());
        }
        return repository.save(task);
    }
}
