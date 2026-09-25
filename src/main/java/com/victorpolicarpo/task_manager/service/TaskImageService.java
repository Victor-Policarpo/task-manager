package com.victorpolicarpo.task_manager.service;

import com.victorpolicarpo.task_manager.dto.taskImage.TaskImageResponseDto;
import com.victorpolicarpo.task_manager.exception.BadRequestException;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskImageService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
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
                () -> new ResourceNotFoundException("Task not found or not exists")
        );
        long currentImages = taskImageRepository.countByTaskId(id);
        if (currentImages + file.size() > maxImagePerTask){
            throw new BadRequestException("You can only upload " + maxImagePerTask + " images per task");
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

    public List<TaskImageResponseDto> getTaskImages(Long taskId) {
        if (!taskRepository.existsById(taskId)){
            throw new ResourceNotFoundException("Task not found or not exists");
        }
        return toResponseDtoListWithPresignedUrls(taskImageRepository.findByTaskId(taskId));
    }

    @Transactional
    public void deleteTaskImage(Long taskId, Long imageId) {
        taskRepository.findById(taskId).orElseThrow(
                        () -> new ResourceNotFoundException("Task not found or not exists")
                );

        TaskImage taskImage = taskImageRepository.findByIdAndTaskId(imageId, taskId).orElseThrow(
                () -> new ResourceNotFoundException("Image not found or not exists")
                );
        deleteImagesFromStorage(List.of(taskImage));
        taskImageRepository.delete(taskImage);
    }

    public List<TaskImageResponseDto> toResponseDtoListWithPresignedUrls(List<TaskImage> images){
        return images.stream()
                .map(image -> {
                    TaskImageResponseDto dto = taskImageMapper.toResponseDto(image);
                    String presignedUrl = generatePresignedUrl(image.getS3Key());
                    dto.setUrl(presignedUrl);
                    return dto;
                })
                .toList();
    }

    public void deleteImagesFromStorage(List<TaskImage> images) {
        images.forEach(image ->
                s3Client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(bucketName)
                                .key(image.getS3Key())
                                .build()
                )
        );
    }

    private String generatePresignedUrl(String s3Key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private String createImagePath(String contentType, Long taskId){
        if (taskId == null){
            throw new BadRequestException("Task ID cannot be null");
        }

        String extension = CONTENT_TYPE_EXTENSIONS.get(contentType);
        if (extension == null){
            throw new BadRequestException("File type is not supported");
        }
        return "task-images/" + taskId + "/" + UUID.randomUUID() + extension;
    }

    private void validateFile(MultipartFile file){
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (!CONTENT_TYPE_EXTENSIONS.containsKey(file.getContentType())) {
            throw new BadRequestException("File type is not supported");
        }

        if (file.getSize() > maxFileSize.toBytes()) {
            throw new BadRequestException("File size exceeds the maximum limit of " +maxFileSize);
        }
    }
}
