package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.task.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.task.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.task.TaskUpdateDto;
import com.victorpolicarpo.task_manager.dto.user.UserMinDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    Task toEntity(TaskRequestDto taskRequestDto);
    TaskResponseDto toResponseDto(Task entity);
    void updateEntityFromDto(TaskUpdateDto taskUpdateDto, @MappingTarget Task entity);
    List<TaskResponseDto> toResponseDtoList(List<Task> entity);
    UserMinDto toUserMinDto(User user);
    UserMinDto map(UserResponseDto value);
}
