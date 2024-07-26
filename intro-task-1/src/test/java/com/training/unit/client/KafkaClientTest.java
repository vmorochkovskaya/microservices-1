package com.training.unit.client;

import com.training.client.KafkaClient;
import com.training.entity.FileDB;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaClientTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaClient kafkaClient;

    @Test
    void sendMessage() {
        FileDB fileDB = new FileDB("data".getBytes(), "123", "location");

        kafkaClient.sendMessage(fileDB);

        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }

    @Test
    void sendMessageException() {
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(any(ProducerRecord.class));

        FileDB fileDB = new FileDB("data".getBytes(), "123", "location");

        assertThrows(RuntimeException.class, () -> {
            kafkaClient.sendMessage(fileDB);
        });

        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }
}
