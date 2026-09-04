package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserMinDto;
import com.victorpolicarpo.task_manager.exception.GlobalExceptionHandler;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private TaskResponseDto taskDto(Long id, String title, String content, boolean completed) {
        UserMinDto user = new UserMinDto();
        user.setId(1L);
        user.setName("Victor");
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCompleted(completed);
        dto.setUser(user);
        return dto;
    }

    @Nested
    class ListTasksTests {
        @Test
        void listAllShouldReturnTasks() throws Exception {
            when(taskService.listAll()).thenReturn(List.of(
                    taskDto(1L, "Task A", "Content A", false)
            ));

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Task A"));
        }

        @Test
        void listAllShouldReturnEmptyWhenNoTasks() throws Exception {
            when(taskService.listAll()).thenReturn(List.of());

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    class GetTaskTests {
        @Test
        void findByIdShouldReturnTask() throws Exception {
            when(taskService.findById(1L)).thenReturn(taskDto(1L, "Task A", "Content A", false));

            mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Task A"));
        }

        @Test
        void findByIdShouldReturn404WhenNotOwned() throws Exception {
            when(taskService.findById(99L)).thenThrow(new ResourceNotFoundException("Task not found"));

            mockMvc.perform(get("/tasks/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateTaskTests {
        @Test
        void createTaskShouldReturn201() throws Exception {
            when(taskService.createTask(any())).thenReturn(taskDto(1L, "New Task", "Content", false));

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "title": "New Task",
                                      "content": "Content"
                                    }
                                    """))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("New Task"));
        }

        @Test
        void createTaskShouldIgnoreUserIdFromClient() throws Exception {
            when(taskService.createTask(any())).thenReturn(taskDto(1L, "Task", "Content", false));

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "title": "Task",
                                      "content": "Content",
                                      "user_id": 999
                                    }
                                    """))
                    .andExpect(status().isCreated());
        }

        @Test
        void createTaskShouldReturn400WhenInvalid() throws Exception {
            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "title": "AB",
                                      "content": "AB"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateTaskTests {
        @Test
        void updateShouldReturnUpdatedTask() throws Exception {
            when(taskService.update(eq(1L), any())).thenReturn(taskDto(1L, "Updated", "Content", false));

            mockMvc.perform(patch("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "title": "Updated"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Updated"));
        }

        @Test
        void updateShouldReturn404WhenNotOwned() throws Exception {
            when(taskService.update(eq(99L), any()))
                    .thenThrow(new ResourceNotFoundException("Task not found"));

            mockMvc.perform(patch("/tasks/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "title": "Hacked"
                                    }
                                    """))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CompleteTaskTests {
        @Test
        void completeShouldReturnCompletedTask() throws Exception {
            when(taskService.taskCompleted(1L)).thenReturn(taskDto(1L, "Task", "Content", true));

            mockMvc.perform(patch("/tasks/1/complete"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.completed").value(true));
        }

        @Test
        void completeShouldReturn404WhenNotOwned() throws Exception {
            when(taskService.taskCompleted(99L))
                    .thenThrow(new ResourceNotFoundException("Task not found"));

            mockMvc.perform(patch("/tasks/99/complete"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteTaskTests {
        @Test
        void deleteShouldReturn204() throws Exception {
            mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteShouldReturn404WhenNotOwned() throws Exception {
            doThrow(new ResourceNotFoundException("Task not found"))
                    .when(taskService).delete(99L);

            mockMvc.perform(delete("/tasks/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class FilterTests {
        @Test
        void filterByStatusShouldReturnFilteredTasks() throws Exception {
            when(taskService.filterByStatus(true)).thenReturn(List.of(
                    taskDto(1L, "Completed", "Content", true)
            ));

            mockMvc.perform(get("/tasks/search").param("completed", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].completed").value(true));
        }
    }
}
