package org.ifsul.carhired.api.aws;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static org.ifsul.carhired.api.system.config.CacheRecords.CACHE_PROFILE_PICTURE;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final ValueOperations<String, String> cacheOps;
    private static final String bucketName = "carhired-vehicles-pictures";

    public String uploadFile(@NotNull MultipartFile file) throws IOException {
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;
        Path tempFile = Files.createTempFile("temp", file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        PutObjectResponse response = s3Client.putObject(putObjectRequest, tempFile);
        Files.delete(tempFile);
        return fileName;
    }


    public String getPresidedUrl(String keyName) {
        String cacheKey = CACHE_PROFILE_PICTURE + ":" + keyName;
        var urlCache = cacheOps.get(cacheKey);
        if (urlCache == null) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            var url = presignedGetObjectRequest.url().toString();
            cacheOps.set(cacheKey, url, Duration.ofHours(1));
            return url;
        }
        return urlCache;
    }
}
