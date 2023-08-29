package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.model.Video;
import com.example.webserveruploadingvideos.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
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
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/user")
public class UserController {

    // Хранение семафоров для каждого пользователя
    private final ConcurrentMap<String, Semaphore> userSemaphores = new ConcurrentHashMap<>();

    private final VideoService videoService;

    public UserController(VideoService uploadVideoService) {
        this.videoService = uploadVideoService;
    }

    // Авторизация
    @PostMapping("/authority")
    public ResponseEntity<String> authority(@RequestBody String userName) {
        return ResponseEntity.ok(videoService.authority(userName));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                              HttpServletResponse response,
                                              Authentication authentication) {

        // Генерируем хэш файла
        String videoHash = videoService.generateVideoHash(file);
        // Получение или создание семафора для пользователя
        Semaphore userSemaphore = userSemaphores
                .computeIfAbsent(videoHash, k -> new Semaphore(2));
        if (!file.isEmpty()) {
            try {

                if (userSemaphore.tryAcquire()) {

                    // старт загрузки
                    LocalDateTime startTime = LocalDateTime.now();

                    // Проверяем, является ли загруженный файл видео и
                    // сохраняем в базу данных информации о видео если токого нет в бвзе данных
                    Boolean uploadVideo = videoService.uploadVideo(videoHash, file.getName(),
                            startTime, authentication);

                    if (videoService.isVideoFile(file) || uploadVideo) {
                        try {

                            // Сохраняем файл на диск
                            byte[] bytes = file.getBytes();
                            long totalBytes = bytes.length;
                            long currentBytes = 0;

                            while (currentBytes < totalBytes) {

                                // Сохраняем файл на диск
                                currentBytes++;
                                int progress = (int) ((currentBytes * 100) / totalBytes);

                                // Отправление обновленной информации о ходе выполнения
                                response.getWriter().write(progress);
                            }

                            Path uploadPath = Paths.get("D:\\TestVideo");
                            Path filePath = uploadPath.resolve(videoHash);
                            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                            LocalDateTime endTime = LocalDateTime.now();
                            videoService.endUploadVideo( videoHash, endTime);
                            return ResponseEntity.ok("Видео успешно загружено!");

                        } catch (IOException e) {
                            return ResponseEntity.badRequest().body("Ошибка при сохранении файла: " + e.getMessage());
                        }
                    } else {
                        return ResponseEntity.badRequest().body("Выбранный файл не является видео!");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Файл для загрузки не выбран!");
                }
            } finally {
                userSemaphore.release(); // Освобождение семафора после загрузки
            }
        } else {
            return ResponseEntity.badRequest().body("Вы уже загрузили максимальное количество видео.");
        }
    }

    // Страница со списком всех видео
    @GetMapping("/all_videos")
    public ResponseEntity<Map<String, String>> getAllVideoUsers(Authentication authentication) {
        Map<String, String> videoUsers = videoService.getAllVideoUsers(authentication);
            return ResponseEntity.ok(videoUsers);
    }

    // скачать видео
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String videoHash) throws IOException {

           Video video = videoService.downloadVideo(videoHash);

        Path path = Paths.get("D:\\TestVideo\\" + videoHash);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + video.getNameVideo())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }






}