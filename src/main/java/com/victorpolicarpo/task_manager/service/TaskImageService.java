package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.exception.ResourceNotFoundException;
import com.victorpolicarpo.task_manager.mapper.TaskImageMapper;
import com.victorpolicarpo.task_manager.model.Task;
import com.victorpolicarpo.task_manager.model.TaskImage;
import com.victorpolicarpo.task_manager.repository.TaskImageRepository;
import com.victorpolicarpo.task_manager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskImageService {
    private final S3Client s3Client;
    private final TaskImageRepository taskImageRepository;
    private final TaskRepository taskRepository;
    private final TaskImageMapper taskImageMapper;

    private static final Map<String, String> CONTENT_TYPE_EXTENSIONS = Map.of(
        "image/jpeg", ".jpeg",
        "image/png", ".png",
        "image/webp", ".webp"
    );

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${app.max-image-per-task}")
    private int maxImagePerTask;

    @Transactional
    public void uploadImages(List<MultipartFile> file, Long id) throws IOException {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task with ID " + id + " does not exist")
        );
        long currentImages = taskImageRepository.countByTaskId(id);
        if (currentImages + file.size() > maxImagePerTask){
            throw new IllegalArgumentException("You can only upload " + maxImagePerTask + " images per task");
        }

        for (MultipartFile multipartFile : file) {
            validateFile(multipartFile);
            String key = createImagePath(multipartFile.getContentType(), id);
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(multipartFile.getContentType())
                            .build(),
                    RequestBody.fromBytes(multipartFile.getBytes())
            );
            TaskImage taskImage = taskImageMapper.toEntity(multipartFile, key, task);
            taskImageRepository.save(taskImage);
        }
    }

    public String createImagePath(String contentType, Long taskId){
        if (taskId == null){
            throw new IllegalArgumentException("Task ID cannot be null");
        }

        String extension = CONTENT_TYPE_EXTENSIONS.get(contentType);
        if (extension == null){
            throw new IllegalArgumentException("File type is not supported");
        }
        return "task-images/" + taskId + "/" + UUID.randomUUID() + extension;
    }

    public void validateFile(MultipartFile file){
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!CONTENT_TYPE_EXTENSIONS.containsKey(file.getContentType())) {
            throw new IllegalArgumentException("File type is not supported");
        }

        if (file.getSize() > maxFileSize.toBytes()) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of " +maxFileSize);
        }
    }
}
