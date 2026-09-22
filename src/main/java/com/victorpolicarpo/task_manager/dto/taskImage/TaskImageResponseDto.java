package com.victorpolicarpo.task_manager.dto.taskImage;

import java.time.LocalDateTime;

public record TaskImageResponseDto(
        Long id,
        String originalFileName,
        String contentType,
        Long fileSize,
        LocalDateTime createdAt,
        String url
) {
}
