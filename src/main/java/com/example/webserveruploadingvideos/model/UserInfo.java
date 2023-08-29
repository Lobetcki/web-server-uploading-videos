package com.example.webserveruploadingvideos.model;

import com.example.webserveruploadingvideos.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class UserInfo {

    @Id
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    private List<Video> downloadableVideo;
}
