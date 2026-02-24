package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.task.TaskMinDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserRequestDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {TaskMapper.class})
public interface UserMapper {
    User toEntity(UserRequestDto userRequestDto);
    UserResponseDto toResponseDto(User entity);
    void updateEntityFromDto(UserUpdateDto userUpdateDto, @MappingTarget User entity);
    List<UserResponseDto> toResponseDtoList(List<User> entity);
    TaskMinDto toUserMinDto(Task task);
    TaskMinDto map(TaskResponseDto value);
}
