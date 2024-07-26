package com.training.client;

import com.training.entity.FileDB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(FileDB outboundEvent) {
        try {
            final ProducerRecord<String, String> record =
                    new ProducerRecord<>("resource", outboundEvent.getId(), outboundEvent.getId());
            kafkaTemplate.send(record);
        } catch (Exception e) {
            String message = "Error sending message to topic " + "resource-event";
            log.error(e.getMessage());
            throw new RuntimeException(message, e);
        }
    }
}
