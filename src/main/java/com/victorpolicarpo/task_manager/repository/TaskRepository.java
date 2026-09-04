package com.victorpolicarpo.task_manager.repository;

import com.victorpolicarpo.task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserIdOrderByIdAsc(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findAllByUserIdAndCompletedOrderByIdAsc(Long userId, boolean completed);
}
