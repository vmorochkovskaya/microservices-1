package com.training.contract;

import com.training.Application;
import com.training.client.FeignSongClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.Nullable;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.LOCAL;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;
import static org.springframework.messaging.support.MessageBuilder.createMessage;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {KafkaConsumerTest.TestConfiguration.class, Application.class})
@AutoConfigureStubRunner(ids = {"org.example:intro-task-1:1.0-SNAPSHOT:stubs:8083"},
        stubsMode = LOCAL)
@Testcontainers
class KafkaConsumerTest {
    @MockBean
    private FeignSongClient feignSongClient;

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        kafka.start();
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    @Autowired
    StubTrigger trigger;

    @Test
    void shouldProcessMessageReceiving() {
        trigger.trigger("triggerResourceCreatedEvent");

        verify(feignSongClient).postSong(any());
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        MessageVerifierSender<Message<?>> standaloneMessageVerifier(KafkaTemplate kafkaTemplate) {
            return new MessageVerifierSender<Message<?>>() {

                @Override
                public void send(Message<?> message, String destination, @Nullable YamlContract contract) {
                    kafkaTemplate.send(message);
                }

                @Override
                public <T> void send(T payload,
                                     Map<String, Object> headers,
                                     String destination,
                                     @Nullable YamlContract contract) {
                    kafkaTemplate.send(createMessage(payload, new MessageHeaders(Map.of(TOPIC, destination)))
                    );
                }
            };
        }
    }


}


