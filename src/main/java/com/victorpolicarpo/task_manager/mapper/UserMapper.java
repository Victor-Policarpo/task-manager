package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.auth.RegisterRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskMinDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {TaskMapper.class})
public interface UserMapper {
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequestDto dto);
    UserResponseDto toResponseDto(User entity);
    List<UserResponseDto> toResponseDtoList(List<User> entity);
    TaskMinDto toTaskMinDto(Task task);
    void toUpdateUser(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
