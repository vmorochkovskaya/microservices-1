package com.training.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Data
@NoArgsConstructor
public class FileDB {
    @Id
    private String id;
    @Lob
    private byte[] data;
    private String s3Location;

    public FileDB(byte[] data, String id, String s3Location){
        this.data = data;
        this.id = id;
        this.s3Location = s3Location;
    }

}
