package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.user.UserRequestDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.UserMapper;
import com.victorpolicarpo.task_manager.model.User;
import com.victorpolicarpo.task_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User userSaved = userRepository.save(user);
        return userMapper.toResponseDto(userSaved);
    }


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
    public UserResponseDto update(@Valid UserUpdateDto userUpdateDto, Long id) {
        User user = findEntityById(id);
        userMapper.updateEntityFromDto(userUpdateDto, user);
        User userSaved = userRepository.save(user);
        return userMapper.toResponseDto(userSaved);
    }

    @Transactional
    public void delete(Long id){
        User user = findEntityById(id);
        userRepository.delete(user);
    }
}
