package com.training.integration.client;

import com.training.client.KafkaClient;
import com.training.entity.FileDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class KafkaClientIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaClient kafkaClient;

    @Test
    public void testSendMessage() {
        String id = UUID.randomUUID().toString();
        FileDB fileDB = new FileDB("test data".getBytes(), id, "2024-07-15/" + id + ".mp3");

        assertDoesNotThrow(() -> kafkaClient.sendMessage(fileDB));
    }
}

