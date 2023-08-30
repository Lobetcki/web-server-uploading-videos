package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.dto.VideoDTO;
import com.example.webserveruploadingvideos.exception.InvalidParametersExeption;
import com.example.webserveruploadingvideos.model.Video;
import com.example.webserveruploadingvideos.service.VideoService;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save")
    public void saveUser(@RequestParam String username) {
        if (!videoService.saveUser(username)) {
            throw new InvalidParametersExeption();
        }

    }

    // Загрузка видео
    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                              @NotNull String username) {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(videoService.uploadVideo(file, username));
        } else {
            return ResponseEntity.badRequest().body("Файл для загрузки не выбран!");
        }
    }

    // Страница со списком всех видео
    @GetMapping("/all_videos")
    public List<VideoDTO> getAllVideoUsers(@NotNull String username) {
        return videoService.getAllVideoUsers(username);
    }

    // скачать видео
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadVideo(@NotNull String videoHash,
                                                  @NotNull String username) {

        Video video = videoService.downloadVideo(videoHash, username);

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