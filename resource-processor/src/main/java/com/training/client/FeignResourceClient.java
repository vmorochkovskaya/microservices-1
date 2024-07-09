package com.training.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="resource", url = "localhost:8083")
public interface FeignResourceClient {
    @GetMapping("/resources/{id}")
    byte[] getResource(@PathVariable("id") String id);
}
