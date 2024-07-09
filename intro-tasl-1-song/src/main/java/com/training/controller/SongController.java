package com.training.controller;

import com.training.annotation.ValidCsvString;
import com.training.dto.SongIdDto;
import com.training.dto.SongIdsDto;
import com.training.entity.Song;
import com.training.exception.ResourceNotFoundException;
import com.training.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/songs")
@Slf4j
@Validated
public class SongController {
    @Autowired
    private SongRepository songRepository;

    @PostMapping
    public ResponseEntity<SongIdDto> postSong(@RequestBody @Valid Song song) {
        Song savedSong = songRepository.save(song);
        System.out.println(savedSong.getId());
        return ResponseEntity.ok().body(new SongIdDto(savedSong.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getSong(@PathVariable Integer id) {
        Optional<Song> song = songRepository.findById(id);
        return song.map(res -> ResponseEntity.ok().body(song))
                .orElseThrow(() -> new ResourceNotFoundException("The resource with the specified id does not exist"));

    }

    @DeleteMapping
    public ResponseEntity<SongIdsDto> deleteSongs(@Valid @ValidCsvString @RequestParam("id") String idCsv) {
        List<Integer> ids = Arrays.stream(idCsv.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<Song> findFiles = songRepository.findAllById(ids::iterator);
        songRepository.deleteAll(findFiles::iterator);
        return ResponseEntity.ok().body(new SongIdsDto(findFiles.stream()
                .map(Song::getId)
                .collect(Collectors.toList())));
    }

}
