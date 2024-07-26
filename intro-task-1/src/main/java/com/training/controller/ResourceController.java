package com.training.controller;

import com.training.annotation.ValidCsvString;
import com.training.annotation.ValidMp3;
import com.training.client.KafkaClient;
import com.training.dto.FileIdDto;
import com.training.dto.FileIdsDto;
import com.training.entity.FileDB;
import com.training.exception.ResourceNotFoundException;
import com.training.repository.ResourceRepository;
import com.training.services.S3CService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
@Slf4j
@Validated
public class ResourceController {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private KafkaClient kafkaClient;
    @Autowired
    private S3CService s3CService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<FileIdDto> postResource(@RequestBody @Valid @ValidMp3 byte[] fileData) {
        //1. S3
        String id = UUID.randomUUID().toString();
        String s3Location = LocalDate.now() + "/" + id + ".mp3";
        s3CService.upload(fileData, s3Location);

        //2. DB
        FileDB fileDb = resourceRepository.save(new FileDB(fileData, id, s3Location));

        //3. Kafka
        kafkaClient.sendMessage(fileDb);

        return ResponseEntity.ok().body(new FileIdDto(fileDb.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable String id) {
        log.info("GET: " + id);
        Optional<FileDB> resource = resourceRepository.findById(id);
        return resource.map(res -> ResponseEntity.ok().body(res.getData()))
                .orElseThrow(() -> new ResourceNotFoundException("The resource with the specified id does not exist"));

    }

    @DeleteMapping
    public ResponseEntity<FileIdsDto> deleteResources(@Valid @ValidCsvString @RequestParam("id") String idCsv) {
        List<String> ids = Arrays.stream(idCsv.split(","))
                .collect(Collectors.toList());
        List<FileDB> findFiles = resourceRepository.findAllById(ids);
        resourceRepository.deleteAll(findFiles);
        return ResponseEntity.ok().body(new FileIdsDto(findFiles.stream()
                .map(FileDB::getId)
                .collect(Collectors.toList())));
    }
}
