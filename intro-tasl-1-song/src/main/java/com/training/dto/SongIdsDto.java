package com.training.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SongIdsDto {
    private List<Integer> ids;
}
