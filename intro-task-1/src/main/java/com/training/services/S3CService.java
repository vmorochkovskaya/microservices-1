package com.training.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3CService {
    private final S3Client s3Client;

    public void upload(byte[] fileData, String location) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("resource-mp3")
                    .key(location)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));
        } catch (Exception se) {
            System.err.println("Service exception thrown: " + se.getMessage());
        }
    }
}
