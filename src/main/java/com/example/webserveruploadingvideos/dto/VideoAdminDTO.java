package com.example.webserveruploadingvideos.dto;

import com.example.webserveruploadingvideos.model.UserInfo;
import com.example.webserveruploadingvideos.model.Video;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VideoAdminDTO {

    private String videoHash;
    private String nameVideo;
    private LocalDateTime startTime;

    private List<String> user;

    public static VideoAdminDTO from(Video video) {
        VideoAdminDTO videoAdminDTO = new VideoAdminDTO();

        videoAdminDTO.setVideoHash(video.getVideoHash());
        videoAdminDTO.setNameVideo(video.getNameVideo());
        videoAdminDTO.setStartTime(video.getStartTime());
        videoAdminDTO.setUser(video.getUser().stream()
                .map(UserInfo::getUserName)
                .collect(Collectors.toList()));

        return videoAdminDTO;
    }


}
