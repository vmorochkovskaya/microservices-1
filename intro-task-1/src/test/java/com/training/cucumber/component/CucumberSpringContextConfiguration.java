package com.training.cucumber.component;

import com.training.Application;
import com.training.client.KafkaClient;
import com.training.services.S3CService;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@CucumberContextConfiguration
@ContextConfiguration(classes = Application.class, loader = SpringBootContextLoader.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CucumberSpringContextConfiguration {
    @MockBean
    private S3CService s3CService;
    @MockBean
    private KafkaClient kafkaClient;

    @Before
    public void setup_cucumber_spring_context(){
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }

}
