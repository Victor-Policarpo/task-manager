package com.victorpolicarpo.task_manager.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private boolean completed = false;

    public Task(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
