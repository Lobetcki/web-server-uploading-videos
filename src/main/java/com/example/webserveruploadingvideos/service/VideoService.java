package com.example.webserveruploadingvideos.service;

import com.example.webserveruploadingvideos.exception.ItNotFoundException;
import com.example.webserveruploadingvideos.model.UserInfo;
import com.example.webserveruploadingvideos.model.Video;
import com.example.webserveruploadingvideos.repozitory.UserRepository;
import com.example.webserveruploadingvideos.repozitory.VideoRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

//    @Value("${telematika.security.token-expired}")
//    private Integer expiredSec;

    public VideoService(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    // авторизация
    public String authority(String userName) {
        UserInfo user = new UserInfo();
        user.setUserName(userName);
        userRepository.existsById(userName);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userName;
    }

    // сохраняем в базу данных информации о видео если токого нет в бвзе данных
    @Transactional
    public boolean uploadVideo(String videoHash, String videoName,
                               LocalDateTime startTime, Authentication authentication) {

        UserInfo user = userRepository.findById(authentication.getName()).orElseThrow();

        if (user != null) {
            Video video = new Video();
            video.setVideoHash(videoHash);
            video.setNameVideo(videoName);
            video.getUser().add(user);
            user.getDownloadableVideo().add(video);

            if (videoRepository.existsById(videoHash)) {
                userRepository.save(user);
                videoRepository.save(video);
                return false;
            } else {
                video.setStartTime(startTime);
                userRepository.save(user);
                videoRepository.save(video);
                return true;
            }
        } else {
            return false;
        }
    }


    // проверка, что файл является видео
    public boolean isVideoFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            return extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("avi");
        }
        return false;
    }

    // Генерируем хэш файла
    public String generateVideoHash(MultipartFile file) {

        try (InputStream is = file.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hash = digest.digest();

            // Преобразование байтов хэша в строку для хранения
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Конец загрузки
    public void endUploadVideo(String videoHash,
                               LocalDateTime endTime) {
        Video video = videoRepository.findById(videoHash).orElse(null);
        if (video != null) {
            video.setEndTime(endTime);
            videoRepository.save(video);
        }
    }

    // Страница со списком всех видео
    public Map<String, String> getAllVideoUsers(Authentication authentication) {
        UserInfo user = userRepository.findById(authentication.getName()).orElseThrow(ItNotFoundException::new);
        return user.getDownloadableVideo()
                .stream()
                .collect(Collectors.toMap(
                        Video::getVideoHash,
                        Video::getNameVideo
                        ));
    }

    // информация о видео
    public Video downloadVideo(String videoHash) {
        return videoRepository.findById(videoHash).orElseThrow(ItNotFoundException::new);
    }
}
