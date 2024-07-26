package com.training.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    private Integer rate;
    private double duration;
    private String resourceId;
}
