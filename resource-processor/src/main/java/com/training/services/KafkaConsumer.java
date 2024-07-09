package com.training.services;

import com.training.client.FeignResourceClient;
import com.training.client.FeignSongClient;
import com.training.utils.Mp3Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private final FeignSongClient feignSongClient;
    private final FeignResourceClient feignResourceClient;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumer.group}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received Message: key={}, value={}, partition={}, offset={}", record.key(), record.value(), record.partition(), record.offset());
        //1. Get Resource
        byte[] resourceBytes = feignResourceClient.getResource(record.value());

        //2. Post Song
        feignSongClient.postSong(Mp3Utils.buildSongFromBytes(resourceBytes, record.value()));
    }
}