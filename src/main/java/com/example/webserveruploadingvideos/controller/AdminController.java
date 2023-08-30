package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.dto.VideoAdminDTO;
import com.example.webserveruploadingvideos.service.VideoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final VideoService videoService;

    public AdminController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/dashboard")
    public List<VideoAdminDTO> adminDashboard(String username) {

        return videoService.sendUploadToAdmin(username);
    }

}


