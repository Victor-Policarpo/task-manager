package com.victorpolicarpo.task_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 50, message = "The field must contain between 3 and 50 characters.")
    @Column(nullable = false, length = 50)
    private String title;

    @NotBlank(message = "Must not be blank.")
    @Size(min = 3, max = 250, message = "The field must contain between 3 and 250 characters.")
    @Column(nullable = false, length = 250)
    private String content;

    @Column
    private boolean completed = false;

}
