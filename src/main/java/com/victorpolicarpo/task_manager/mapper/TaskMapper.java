package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.TaskRequestDto;
import com.victorpolicarpo.task_manager.dto.TaskResponseDto;
import com.victorpolicarpo.task_manager.dto.TaskUpdateDto;
import com.victorpolicarpo.task_manager.model.Task;
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

}
