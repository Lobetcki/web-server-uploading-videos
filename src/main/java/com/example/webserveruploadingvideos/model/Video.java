package com.example.webserveruploadingvideos.model;

import com.example.webserveruploadingvideos.enums.StatusVideo;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private StatusVideo status;

    @ManyToMany
    private List<UserInfo> user;

}
