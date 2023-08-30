package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.dto.VideoDTO;
import com.example.webserveruploadingvideos.exception.InvalidParametersExeption;
import com.example.webserveruploadingvideos.model.Video;
import com.example.webserveruploadingvideos.service.VideoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final VideoService videoService;

    public UserController(VideoService uploadVideoService) {
        this.videoService = uploadVideoService;
    }

    // Загрузка видео
    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,

                                              Authentication authentication) {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(videoService.uploadVideo(file, authentication));
        } else {
            return ResponseEntity.badRequest().body("Файл для загрузки не выбран!");
        }
    }

    // Страница со списком всех видео
    @GetMapping("/all_videos")
    public List<VideoDTO> getAllVideoUsers(Authentication authentication) {
        return videoService.getAllVideoUsers(authentication);
    }

    // скачать видео
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String videoHash,
                                                  Authentication authentication) {

        Video video = videoService.downloadVideo(videoHash, authentication);

        try {
        Path path = Paths.get("D:\\TestVideo\\" + videoHash);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + video.getNameVideo())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
        } catch (IOException e) {
            throw new InvalidParametersExeption();
        }
    }
}