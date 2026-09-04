package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.model.Role;
import com.victorpolicarpo.task_manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    private UserResponseDto userDto(Long id, String name, String email, Role role) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setAge(25);
        dto.setEmail(email);
        dto.setRole(role);
        dto.setTasks(List.of());
        return dto;
    }

    @Nested
    class ProfileTests {
        @Test
        void getProfileShouldReturnAuthenticatedUser() throws Exception {
            when(userService.getProfile()).thenReturn(userDto(1L, "Victor", "victor@example.com", Role.USER));

            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Victor"))
                    .andExpect(jsonPath("$.email").value("victor@example.com"))
                    .andExpect(jsonPath("$.role").value("USER"))
                    .andExpect(jsonPath("$.passwordHash").doesNotExist())
                    .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        void updateProfileShouldReturnUpdatedUser() throws Exception {
            when(userService.updateProfile(any())).thenReturn(userDto(1L, "Victor Updated", "victor@example.com", Role.USER));

            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "name": "Victor Updated"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Victor Updated"));
        }

        @Test
        void profileShouldNotExposePasswordHash() throws Exception {
            when(userService.getProfile()).thenReturn(userDto(1L, "Victor", "victor@example.com", Role.USER));

            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.passwordHash").doesNotExist())
                    .andExpect(jsonPath("$.password").doesNotExist())
                    .andExpect(jsonPath("$.token").doesNotExist());
        }

        @Test
        void profileShouldIncludeRole() throws Exception {
            when(userService.getProfile()).thenReturn(userDto(1L, "Victor", "victor@example.com", Role.ADMIN));

            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.role").value("ADMIN"));
        }
    }

    @Nested
    class AdminEndpointsTests {
        @Test
        void listAllUsersShouldReturn200() throws Exception {
            when(userService.listAll()).thenReturn(List.of(
                    userDto(1L, "Victor", "victor@example.com", Role.USER)
            ));

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Victor"));
        }

        @Test
        void findByIdShouldReturn200() throws Exception {
            when(userService.findById(1L)).thenReturn(userDto(1L, "Victor", "victor@example.com", Role.USER));

            mockMvc.perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Victor"));
        }

        @Test
        void deleteUserShouldReturn204() throws Exception {
            mockMvc.perform(delete("/users/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
