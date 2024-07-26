package com.training.cucumber.component.steps;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.dto.FileIdDto;
import com.training.entity.FileDB;
import com.training.repository.ResourceRepository;
import com.training.services.S3CService;
import com.training.client.KafkaClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceServiceSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private S3CService s3CService;

    @Autowired
    private KafkaClient kafkaClient;

    private MockMvc mockMvc;
    private byte[] fileData;
    private String resourceId;
    private String resourceId2;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @When("the file is uploaded via the postResource endpoint")
    public void theFileIsUploadedViaThePostResourceEndpoint() throws Exception {
        fileData = Files.readAllBytes(Paths.get("src/test/resources/sample.mp3"));
        String responseContent = mockMvc.perform(post("/resources")
                        .content(fileData)
                        .contentType("audio/mpeg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        FileIdDto jsonResponse = new ObjectMapper().readValue(responseContent, FileIdDto.class);
        resourceId = jsonResponse.getId();
    }

    @Then("the file should be stored in the database")
    public void theFileShouldBeStoredInTheDatabase() {
        Optional<FileDB> fileDB = resourceRepository.findById(resourceId);
        assertThat(fileDB.isPresent()).isTrue();
        assertThat(fileDB.get().getData()).isEqualTo(fileData);
    }

    @Then("a Kafka message should be sent")
    public void aKafkaMessageShouldBeSent() {
        verify(kafkaClient).sendMessage(any());
    }

    @Given("an existing resource in the database")
    public void anExistingResourceInTheDatabase() throws IOException {
        resourceId = UUID.randomUUID().toString();
        fileData = Files.readAllBytes(Paths.get("src/test/resources/sample.mp3"));
        FileDB fileDB = new FileDB(fileData, resourceId, "2024-07-15/" + resourceId + ".mp3");
        resourceRepository.save(fileDB);
    }

    @When("the resource is requested via the getResource endpoint")
    public void theResourceIsRequestedViaTheGetResourceEndpoint() throws Exception {
        mockMvc.perform(get("/resources/{id}", resourceId))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileData));
    }

    @Then("the correct resource data should be returned")
    public void theCorrectResourceDataShouldBeReturned() {
        Optional<FileDB> fileDB = resourceRepository.findById(resourceId);
        assertThat(fileDB.isPresent()).isTrue();
        assertThat(fileDB.get().getData()).isEqualTo(fileData);
    }

    @Given("multiple existing resources in the database")
    public void multipleExistingResourcesInTheDatabase() {
        resourceId = UUID.randomUUID().toString();
        resourceId2 = UUID.randomUUID().toString();
        FileDB fileDB1 = new FileDB("test data 1".getBytes(), resourceId, "2024-07-15/" + resourceId + ".mp3");
        FileDB fileDB2 = new FileDB("test data 2".getBytes(), resourceId2, "2024-07-15/" + resourceId + ".mp3");
        resourceRepository.save(fileDB1);
        resourceRepository.save(fileDB2);
    }

    @When("the resources are deleted via the deleteResources endpoint")
    public void theResourcesAreDeletedViaTheDeleteResourcesEndpoint() throws Exception {
        mockMvc.perform(delete("/resources")
                        .param("id", resourceId + "," + resourceId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ids").value(org.hamcrest.Matchers.containsInAnyOrder(resourceId,resourceId2)));
    }

    @Then("the resources should be removed from the database")
    public void theResourcesShouldBeRemovedFromTheDatabase() {
        Optional<FileDB> deletedFile1 = resourceRepository.findById(resourceId);
        Optional<FileDB> deletedFile2 = resourceRepository.findById(resourceId2);
        assertThat(deletedFile1).isEmpty();
        assertThat(deletedFile2).isEmpty();
    }

}
