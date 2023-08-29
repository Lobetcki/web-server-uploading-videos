package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.dto.VideoAdminDTO;
import com.example.webserveruploadingvideos.service.VideoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VideoService videoService;

    public AdminController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/dashboard")
    public List<VideoAdminDTO> adminDashboard(Authentication authentication) {
        List<VideoAdminDTO> currentUploads = videoService.sendUploadToAdmin(authentication);

        return currentUploads;
    }

}


