package com.training.integration.service;

import com.training.repository.ResourceRepository;
import com.training.services.S3CService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Testcontainers
@DisabledOnOs(OS.WINDOWS)
public class S3ServiceIntegrationTest {

    private static final DockerImageName LOCALSTACK_IMAGE =
            DockerImageName.parse("localstack/localstack:latest");

    private S3Client s3Client;

    @Autowired
    private S3CService s3CService;

    @MockBean
    private ResourceRepository resourceRepository;

    @Container
    public static LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(LocalStackContainer.Service.S3);

    @DynamicPropertySource
    static void registerLocalStackProperties(DynamicPropertyRegistry registry) {
        registry.add("cloud.aws.region.static", localStackContainer::getRegion);
        registry.add("cloud.aws.s3.endpoint", () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3).toString());
        registry.add("cloud.aws.credentials.access-key", () -> "test");
        registry.add("cloud.aws.credentials.secret-key", () -> "test");
    }

    @BeforeEach
    public void setUp() {
        s3Client = S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .build();

        s3Client.createBucket(CreateBucketRequest.builder().bucket("resource-mp3").build());
    }

    @Test
    public void testUpload() {
        byte[] fileData = "test mp3 data".getBytes();
        String location = "2024-07-15/test.mp3";

        assertDoesNotThrow(() -> s3CService.upload(fileData, location));
    }
}

