package com.training.contract;

import com.training.Application;
import com.training.client.KafkaClient;
import com.training.entity.FileDB;
import com.training.repository.ResourceRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

//java.lang.IllegalStateException: org.springframework.beans.factory.NoSuchBeanDefinitionException:
// No bean named 'resource' available


//@SpringBootTest(webEnvironment = NONE, classes = {Application.class, BaseContractTest.TestConfig.class})
//@AutoConfigureMessageVerifier
//@Testcontainers
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public abstract class BaseContractTest {

//    @Container
//    static KafkaContainer kafka =
//            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
//
//    @MockBean
//    private ResourceRepository resourceRepository;
//    @Autowired
//    private KafkaClient kafkaClient;
//
//    @DynamicPropertySource
//    static void kafkaProperties(DynamicPropertyRegistry registry) {
//        kafka.start();
//        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
//    }
//
//    void createResource() {
//        FileDB fileDB = new FileDB();
//        fileDB.setId("7fd76e07-31a2-401f-a25b-24906e2c4a1d");
//        kafkaClient.sendMessage(fileDB);
//    }
//
//    @Configuration
//    @EnableKafka
//    static class TestConfig {
//
//        @Bean
//        KafkaEventVerifier kafkaEventVerifier(){
//            return new KafkaEventVerifier();
//        }
//
//    }
//    static class KafkaEventVerifier implements MessageVerifierReceiver<Message<?>> {
//
//        private final Set<Message> consumedEvents =
//                Collections.synchronizedSet(new HashSet<Message>());
//
//        @KafkaListener(topics = "resource", groupId = "my-consumer-group")
//        void consumeOrderCreated(ConsumerRecord payload) {
//            consumedEvents.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(emptyMap())));
//        }
//
//        @Override
//        public Message receive(String destination,
//                               long timeout,
//                               TimeUnit timeUnit,
//                               @Nullable YamlContract contract) {
//            for (int i = 0; i < timeout; i++) {
//                Message msg = consumedEvents.stream().findFirst().orElse(null);
//                if (msg != null) {
//                    return msg;
//                }
//
//                try {
//                    timeUnit.sleep(1);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            return consumedEvents.stream().findFirst().orElse(null);
//        }
//
//        @Override
//        public Message receive(String destination, YamlContract contract) {
//            return receive(destination, 5, SECONDS, contract);
//        }
//    }

}


