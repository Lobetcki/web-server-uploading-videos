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
    @Column(nullable = false, unique = true)
    private String userName;

    private String password = " ";

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Video> downloadableVideo;
}
