package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.taskImage.TaskImageResponseDto;
import com.victorpolicarpo.task_manager.model.TaskImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskImageMapper {
    TaskImageResponseDto toResponseDto(TaskImage entity);
}
