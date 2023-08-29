package com.example.webserveruploadingvideos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Video {

    @Id
    private String videoHash;
    private String nameVideo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private StatusVideo status;

    @ManyToMany
    private List<UserInfo> user;

}
