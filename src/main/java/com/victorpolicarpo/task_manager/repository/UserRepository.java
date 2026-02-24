package com.victorpolicarpo.task_manager.repository;

import com.victorpolicarpo.task_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
