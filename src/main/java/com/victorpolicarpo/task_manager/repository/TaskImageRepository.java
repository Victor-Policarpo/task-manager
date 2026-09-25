package com.victorpolicarpo.task_manager.repository;

import com.victorpolicarpo.task_manager.model.TaskImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
    long countByTaskId(Long taskId);
}
