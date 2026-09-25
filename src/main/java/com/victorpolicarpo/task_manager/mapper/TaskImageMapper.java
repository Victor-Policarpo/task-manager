package com.victorpolicarpo.task_manager.mapper;

import com.victorpolicarpo.task_manager.dto.taskImage.TaskImageResponseDto;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.TaskImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskImageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "s3Key", source = "key")
    @Mapping(target = "originalFilename", source = "file.originalFilename")
    @Mapping(target = "contentType", source = "file.contentType")
    @Mapping(target = "fileSize", source = "file.size")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "task", source = "task")
    TaskImage toEntity(MultipartFile file, String key, Task task);
    @Mapping(target = "originalFileName", source = "originalFilename")
    @Mapping(target = "url", ignore = true)
    TaskImageResponseDto toResponseDto(TaskImage entity);
}
