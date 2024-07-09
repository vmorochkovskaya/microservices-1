package com.training.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileIdsDto {
    private List<String> ids;
}
