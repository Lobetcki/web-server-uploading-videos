package com.example.webserveruploadingvideos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class UserInfo {

    @Id
    private String userName;

    @ManyToMany
    private List<Video> downloadableVideo;
}
