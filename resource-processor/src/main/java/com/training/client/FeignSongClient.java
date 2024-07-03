package com.training.client;

import com.training.entity.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="song")
public interface FeignSongClient {
    @PostMapping("/songs")
    void postSong(Song song);
}
