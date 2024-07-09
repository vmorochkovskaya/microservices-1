package com.training.client;

import com.training.entity.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="resource")
public interface FeignResourceClient {
    @PostMapping("/resources")
    void postResource(Song song);
}
