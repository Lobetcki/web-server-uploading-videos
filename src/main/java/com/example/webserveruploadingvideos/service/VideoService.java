package com.example.webserveruploadingvideos.service;

import com.example.webserveruploadingvideos.dto.VideoAdminDTO;
import com.example.webserveruploadingvideos.dto.VideoDTO;
import com.example.webserveruploadingvideos.enums.Role;
import com.example.webserveruploadingvideos.enums.StatusVideo;
import com.example.webserveruploadingvideos.exception.ItNotFoundException;
import com.example.webserveruploadingvideos.model.UserInfo;
import com.example.webserveruploadingvideos.model.Video;
import com.example.webserveruploadingvideos.repozitory.UserRepository;
import com.example.webserveruploadingvideos.repozitory.VideoRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {


    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public VideoService(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    public boolean saveUser(String username) {

        UserInfo userFromDB = userRepository.findById(username).orElse(null);

        if (userFromDB != null) {
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setRole(Role.USER);
        userRepository.save(userInfo);
        return true;
    }

    // Созраняем видео на жесткий диск и сохраняем в базу данных информации о видео
    @Transactional
    public String uploadVideo(MultipartFile file,
                               String username
                               ) {
        // Генерируем хэш файла
        String videoHash = generateVideoHash(file);

        UserInfo user = userRepository.findById(username)
                .orElseThrow(ItNotFoundException::new);

        // проверяем количество загружаемых видео
        List<Video> videosUploaded = user.getDownloadableVideo().stream()
                .filter(video -> video.getStatus().equals(StatusVideo.VIDEO_BEING_UPLOADED))
                .toList();

        if (videosUploaded.size() > 2) {
            return "Вы уже загружаете максимальное количество видео.";
        }

        if (isVideoFile(file)) { // проверка я вляется файлом или нет

            Video video = videoRepository.findById(videoHash).orElse(null);


            if (video == null) { // если видео нет в БД
                // старт загрузки
                LocalDateTime startTime = LocalDateTime.now();
                Video videoNew = new Video();
                videoNew.setVideoHash(videoHash);
                videoNew.setNameVideo(file.getOriginalFilename());

                videoNew.setStartTime(startTime);
                videoNew.setStatus(StatusVideo.VIDEO_BEING_UPLOADED);

                List<UserInfo> userInfoList = new ArrayList<>();
                userInfoList.add(user);

                videoNew.setUser(userInfoList);
                user.getDownloadableVideo().add(videoNew);
                videoRepository.save(videoNew);
                userRepository.save(user);

                try {
                    // Сохраняем файл на диск
                    Path uploadPath = Paths.get("D:\\TestVideo");
                    Path filePath = uploadPath.resolve(videoHash);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    endUploadVideo(videoHash, LocalDateTime.now());

                    return "Видео успешно загружено!";

                } catch (IOException e) {
                    return "Ошибка при сохранении файла: " + e.getMessage();
                }
            } else {
                if (user.getDownloadableVideo().contains(video)) {
                    return "Вы уже загрузили рвнее видео " + video.getNameVideo();
                }
                user.getDownloadableVideo().add(video);
                video.getUser().add(user);
                videoRepository.save(video);
                userRepository.save(user);
                return "Видео успешно загружено!";
            }
        } else {
            return "Выбранный файл не является видео!";
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
    @Transactional
    public void endUploadVideo(String videoHash,
                               LocalDateTime endTime) {
        Video video = videoRepository.findById(videoHash).orElse(null);
        if (video != null) {
            video.setEndTime(endTime);
            video.setStatus(StatusVideo.VIDEO_UPLOADED);
            videoRepository.save(video);
        }
    }

    // Страница со списком всех видео
    public List<VideoDTO> getAllVideoUsers(String username) {
        UserInfo user = userRepository.findByUserNameWithVideos(username).orElseThrow(ItNotFoundException::new);
        return user.getDownloadableVideo()
                .stream()
                .map(VideoDTO::from)
                .collect(Collectors.toList());
    }

    // информация о видео
    @Transactional
    public Video downloadVideo(String videoHash, String username) {
        UserInfo user = userRepository.findById(username)
                .orElseThrow(ItNotFoundException::new);

        return user.getDownloadableVideo().stream()
                .filter(video1 -> video1.getVideoHash().equals(videoHash))
                .findFirst().orElseThrow(ItNotFoundException::new);
    }

    // Метод для отправки данных о текущих загрузках на клиентскую сторону
    @Transactional
    @Scheduled(fixedDelay = 1000) // Регулярное выполнение каждую секунду
    public List<VideoAdminDTO> sendUploadToAdmin(String username) {

        UserInfo user = userRepository.findById(username)
                .orElseThrow(ItNotFoundException::new);

        if (user.getRole().equals(Role.ADMIN)) {
            List<Video> currentUploads = videoRepository
                    .findAllByStatus(StatusVideo.VIDEO_BEING_UPLOADED);

            return currentUploads.stream()
                    .map(VideoAdminDTO::from)
                    .collect(Collectors.toList());
        } else {
            throw new ItNotFoundException();
        }
    }


}
