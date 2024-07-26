package com.training.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "song")
@Data
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")
    private int id;

    private int rate;
    private Double duration;
    @NotNull(message = "resourceId cannot be Null")
    private String resourceId;

}
