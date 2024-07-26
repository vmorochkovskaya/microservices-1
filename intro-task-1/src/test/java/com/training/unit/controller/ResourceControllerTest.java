package com.training.unit.controller;

import com.training.client.KafkaClient;
import com.training.controller.ResourceController;
import com.training.dto.FileIdDto;
import com.training.dto.FileIdsDto;
import com.training.entity.FileDB;
import com.training.exception.ResourceNotFoundException;
import com.training.repository.ResourceRepository;
import com.training.services.S3CService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceControllerTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private KafkaClient kafkaClient;

    @Mock
    private S3CService s3CService;

    @InjectMocks
    private ResourceController resourceController;

    @Test
    void postResource() {
        byte[] fileData = "test mp3 data".getBytes();
        FileDB fileDB = new FileDB(fileData, UUID.randomUUID().toString(), "2024-07-15/file.mp3");

        when(resourceRepository.save(any(FileDB.class))).thenReturn(fileDB);

        ResponseEntity<FileIdDto> response = resourceController.postResource(fileData);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fileDB.getId(), response.getBody().getId());
        verify(s3CService).upload(eq(fileData), anyString());
        verify(kafkaClient).sendMessage(any(FileDB.class));
    }

    @Test
    void getResource() {
        byte[] fileData = "test mp3 data".getBytes();
        FileDB fileDB = new FileDB(fileData, UUID.randomUUID().toString(), "2024-07-15/file.mp3");

        when(resourceRepository.findById(anyString())).thenReturn(Optional.of(fileDB));

        ResponseEntity<byte[]> response = resourceController.getResource(fileDB.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(fileData, response.getBody());
    }

    @Test
    void getResourceNotFound() {
        when(resourceRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            resourceController.getResource(UUID.randomUUID().toString());
        });
    }

    @Test
    void deleteResources() {
        List<String> ids = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<FileDB> files = Arrays.asList(
                new FileDB("data1".getBytes(), ids.get(0), "2024-07-15/file1.mp3"),
                new FileDB("data2".getBytes(), ids.get(1), "2024-07-15/file2.mp3")
        );

        when(resourceRepository.findAllById(ids)).thenReturn(files);

        ResponseEntity<FileIdsDto> response = resourceController.deleteResources(String.join(",", ids));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ids, response.getBody().getIds());
        verify(resourceRepository).deleteAll(files);
    }
}
