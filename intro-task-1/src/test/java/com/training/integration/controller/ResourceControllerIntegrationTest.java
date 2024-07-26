package com.training.integration.controller;

import com.training.entity.FileDB;
import com.training.repository.ResourceRepository;
import com.training.services.S3CService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ResourceControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private S3CService s3CService;

    @MockBean
    private S3Client s3Client;

    private MockMvc mockMvc;

    private byte[] fileData;

    @BeforeEach
    public void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        fileData = Files.readAllBytes(Paths.get("src/test/resources/sample.mp3"));
    }

    @Test
    public void testPostResource() throws Exception {
        mockMvc.perform(post("/resources")
                        .content(fileData)
                        .contentType("audio/mpeg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetResource() throws Exception {
        String id = UUID.randomUUID().toString();
        FileDB fileDB = new FileDB(fileData, id, "2024-07-15/" + id + ".mp3");
        resourceRepository.save(fileDB);

        mockMvc.perform(get("/resources/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileDB.getData()));
    }

    @Test
    public void testDeleteResources() throws Exception {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        FileDB fileDB1 = new FileDB(fileData, id1, "2024-07-15/" + id1 + ".mp3");
        FileDB fileDB2 = new FileDB(fileData, id2, "2024-07-15/" + id2 + ".mp3");
        resourceRepository.save(fileDB1);
        resourceRepository.save(fileDB2);

        mockMvc.perform(delete("/resources")
                        .param("id", id1 + "," + id2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ids").isArray())
                .andExpect(jsonPath("$.ids").value(org.hamcrest.Matchers.containsInAnyOrder(id1, id2)));

        Optional<FileDB> deletedFile1 = resourceRepository.findById(id1);
        Optional<FileDB> deletedFile2 = resourceRepository.findById(id2);
        assertThat(deletedFile1).isEmpty();
        assertThat(deletedFile2).isEmpty();
    }
}
