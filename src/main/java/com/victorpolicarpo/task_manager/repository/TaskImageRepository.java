package com.victorpolicarpo.task_manager.repository;

import com.victorpolicarpo.task_manager.model.TaskImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
    long countByTaskId(Long taskId);

    List<TaskImage> findByTaskId(Long taskId);

    Optional<TaskImage> findByIdAndTaskId(Long imageId, Long taskId);
}
