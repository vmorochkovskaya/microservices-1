package com.training.client;

import com.training.configuration.FeignClientConfiguration;
import com.training.dto.SongIdDto;
import com.training.entity.Song;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="song", url = "localhost:8084", configuration = FeignClientConfiguration.class)
public interface FeignSongClient {
    @PostMapping("/songs")
    SongIdDto postSong(Song song);
}
