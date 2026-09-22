package com.victorpolicarpo.task_manager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskImageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public void uploadImages(List<MultipartFile> file, Long id) throws IOException {
        for (MultipartFile multipartFile : file) {
            if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
                throw new IllegalArgumentException("File is empty");
            }
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(multipartFile.getOriginalFilename())
                            .build(),
                    RequestBody.fromBytes(multipartFile.getBytes())

            );
        }

    }
}
