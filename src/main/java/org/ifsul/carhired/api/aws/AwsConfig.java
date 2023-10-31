package org.ifsul.carhired.api.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static software.amazon.awssdk.regions.Region.US_EAST_1;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner
                .builder()
                .build();
    }

}
