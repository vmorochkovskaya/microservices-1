package com.training.cucumber.e2e.steps;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class ResourceProcessingSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private ResultActions resultActions;

    @Before
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Given("a new resource {string} is posted to the Resource Service")
    public void aNewResourceIsPostedToTheResourceService(String fileName) throws Exception {
        byte[] fileData = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
        resultActions = mockMvc.perform(post("/resources")
                .content(fileData)
                .contentType("audio/mpeg"));
    }

    @Then("the Resource Service should return {int}")
    public void theResourceProcessorServiceProcessesTheResourceEvent(int statusCode) throws Exception {
        if (HttpStatus.valueOf(statusCode).equals(HttpStatus.OK)) {
            resultActions.andExpect(status().isOk()).
                    andExpect(jsonPath("$.id").exists());
        }
    }
}

