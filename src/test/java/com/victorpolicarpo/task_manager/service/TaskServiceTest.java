package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.task.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.task.TaskUpdateDto;
import com.victorpolicarpo.task_manager.dto.user.UserMinDto;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.TaskMapper;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.model.Role;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import com.victorpolicarpo.task_manager.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private AuthenticatedUser authenticatedUser;

    private TaskService taskService;

    private User userA;
    private User userB;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(repository, taskMapper, authenticatedUser);
        userA = user(1L, "User A", "a@example.com");
        userB = user(2L, "User B", "b@example.com");
    }

    private User user(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(25);
        user.setEmail(email);
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        return user;
    }

    private Task ownedTask(Long id, String title, User owner) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setContent("Content");
        task.setCompleted(false);
        task.setUser(owner);
        return task;
    }

    private TaskResponseDto responseDto(Long id, String title) {
        UserMinDto userMin = new UserMinDto();
        userMin.setId(1L);
        userMin.setName("User A");
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setContent("Content");
        dto.setCompleted(false);
        dto.setUser(userMin);
        return dto;
    }

    @Nested
    class OwnershipTests {

        @Test
        void listAllShouldReturnOnlyAuthenticatedUsersTasks() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task taskA = ownedTask(1L, "Task A", userA);
            when(repository.findAllByUserIdOrderByIdAsc(1L)).thenReturn(List.of(taskA));
            when(taskMapper.toResponseDtoList(any())).thenReturn(List.of(responseDto(1L, "Task A")));

            List<TaskResponseDto> result = taskService.listAll();

            assertEquals(1, result.size());
            assertEquals("Task A", result.get(0).getTitle());
            verify(repository).findAllByUserIdOrderByIdAsc(1L);
        }

        @Test
        void listAllShouldNotReturnOtherUsersTasks() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findAllByUserIdOrderByIdAsc(1L)).thenReturn(List.of());
            when(taskMapper.toResponseDtoList(any())).thenReturn(List.of());

            List<TaskResponseDto> result = taskService.listAll();

            assertTrue(result.isEmpty());
        }

        @Test
        void findByIdShouldReturnOwnTask() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task taskA = ownedTask(1L, "Task A", userA);
            when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(taskA));
            when(taskMapper.toResponseDto(taskA)).thenReturn(responseDto(1L, "Task A"));

            TaskResponseDto result = taskService.findById(1L);

            assertEquals("Task A", result.getTitle());
        }

        @Test
        void findByIdShouldThrowWhenTaskBelongsToOtherUser() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> taskService.findById(99L));
        }

        @Test
        void updateShouldThrowWhenTaskBelongsToOtherUser() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

            TaskUpdateDto updateDto = new TaskUpdateDto();
            updateDto.setTitle("Hacked");

            assertThrows(ResourceNotFoundException.class, () -> taskService.update(99L, updateDto));
        }

        @Test
        void completeShouldThrowWhenTaskBelongsToOtherUser() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> taskService.taskCompleted(99L));
        }

        @Test
        void deleteShouldThrowWhenTaskBelongsToOtherUser() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> taskService.delete(99L));
            verify(repository, never()).delete(any());
        }

        @Test
        void deleteShouldDeleteOwnTask() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task taskA = ownedTask(1L, "Task A", userA);
            when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(taskA));

            taskService.delete(1L);

            verify(repository).delete(taskA);
        }

        @Test
        void createTaskShouldAssignAuthenticatedUserAsOwner() {
            when(authenticatedUser.get()).thenReturn(userA);
            TaskRequestDto dto = new TaskRequestDto();
            dto.setTitle("New Task");
            dto.setContent("Content");

            Task savedTask = ownedTask(1L, "New Task", userA);
            when(repository.save(any(Task.class))).thenReturn(savedTask);
            when(taskMapper.toResponseDto(any())).thenReturn(responseDto(1L, "New Task"));

            taskService.createTask(dto);

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
            verify(repository).save(captor.capture());
            Task saved = captor.getValue();
            assertEquals(userA, saved.getUser());
            assertEquals("New Task", saved.getTitle());
        }

        @Test
        void createTaskShouldNotTrustUserIdFromClient() {
            when(authenticatedUser.get()).thenReturn(userA);
            TaskRequestDto dto = new TaskRequestDto();
            dto.setTitle("Task");
            dto.setContent("Content");

            Task savedTask = ownedTask(1L, "Task", userA);
            when(repository.save(any(Task.class))).thenReturn(savedTask);
            when(taskMapper.toResponseDto(any())).thenReturn(responseDto(1L, "Task"));

            taskService.createTask(dto);

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
            verify(repository).save(captor.capture());
            assertEquals(userA.getId(), captor.getValue().getUser().getId());
        }

        @Test
        void filterByStatusShouldReturnOnlyAuthenticatedUsersTasks() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task completedTask = ownedTask(1L, "Completed", userA);
            completedTask.setCompleted(true);
            when(repository.findAllByUserIdAndCompletedOrderByIdAsc(1L, true)).thenReturn(List.of(completedTask));
            when(taskMapper.toResponseDtoList(any())).thenReturn(List.of(responseDto(1L, "Completed")));

            List<TaskResponseDto> result = taskService.filterByStatus(true);

            assertEquals(1, result.size());
            verify(repository).findAllByUserIdAndCompletedOrderByIdAsc(1L, true);
        }

        @Test
        void updateShouldModifyOwnTask() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task taskA = ownedTask(1L, "Original", userA);
            when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(taskA));

            TaskUpdateDto updateDto = new TaskUpdateDto();
            updateDto.setTitle("Updated");
            Task updated = ownedTask(1L, "Updated", userA);
            when(repository.save(any())).thenReturn(updated);
            when(taskMapper.toResponseDto(updated)).thenReturn(responseDto(1L, "Updated"));

            TaskResponseDto result = taskService.update(1L, updateDto);

            verify(taskMapper).updateEntityFromDto(updateDto, taskA);
            assertEquals("Updated", result.getTitle());
        }

        @Test
        void completeShouldModifyOwnTask() {
            when(authenticatedUser.getId()).thenReturn(1L);
            Task taskA = ownedTask(1L, "Task A", userA);
            when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(taskA));
            when(repository.save(any())).thenReturn(taskA);
            when(taskMapper.toResponseDto(taskA)).thenReturn(responseDto(1L, "Task A"));

            taskService.taskCompleted(1L);

            assertTrue(taskA.isCompleted());
            verify(repository).save(taskA);
        }
    }

    @Nested
    class FilterByStatusTests {
        @Test
        void filterByCompletedTrueShouldQueryCorrectly() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findAllByUserIdAndCompletedOrderByIdAsc(1L, true)).thenReturn(List.of());
            when(taskMapper.toResponseDtoList(any())).thenReturn(List.of());

            taskService.filterByStatus(true);

            verify(repository).findAllByUserIdAndCompletedOrderByIdAsc(1L, true);
        }

        @Test
        void filterByCompletedFalseShouldQueryCorrectly() {
            when(authenticatedUser.getId()).thenReturn(1L);
            when(repository.findAllByUserIdAndCompletedOrderByIdAsc(1L, false)).thenReturn(List.of());
            when(taskMapper.toResponseDtoList(any())).thenReturn(List.of());

            taskService.filterByStatus(false);

            verify(repository).findAllByUserIdAndCompletedOrderByIdAsc(1L, false);
        }
    }
}
