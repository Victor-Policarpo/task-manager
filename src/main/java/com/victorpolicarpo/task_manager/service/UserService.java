package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.user.UserRequestDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.exception.ConflictException;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.UserMapper;
import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.repository.UserRepository;
import com.victorpolicarpo.task_manager.security.AuthenticatedUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticatedUser authenticatedUser;

    public UserResponseDto findById(Long id) {
        User user = findEntityById(id);
        return userMapper.toResponseDto(user);
    }

    public List<UserResponseDto> listAll() {
        List<User> users = userRepository.findAll(Sort.by("id").ascending());
        return userMapper.toResponseDtoList(users);
    }

    public User findEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User id not found or not exist."));
    }

    @Transactional
    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    public UserResponseDto getProfile() {
        User user = authenticatedUser.get();
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(UserUpdateDto userUpdateDto) {
        User user = authenticatedUser.get();
        applyUpdate(userUpdateDto, user);
        User userSaved = userRepository.save(user);
        return userMapper.toResponseDto(userSaved);
    }

    private void applyUpdate(UserUpdateDto dto, User user) {
        if (dto.getEmail() != null) {
            String normalizedEmail = dto.getEmail().trim().toLowerCase(Locale.ROOT);
            String currentEmail = user.getEmail();
            if (!normalizedEmail.equals(currentEmail) && userRepository.existsByEmail(normalizedEmail)) {
                throw new ConflictException("Email already registered.");
            }
            userMapper.toUpdateUser(dto, user);
        }
    }
}
