package com.victorpolicarpo.task_manager.repository;

import com.victorpolicarpo.task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
