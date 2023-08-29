package com.example.webserveruploadingvideos.dto;

import com.example.webserveruploadingvideos.model.Video;
import lombok.Data;

@Data
public class VideoDTO {

    private String videoHash;
    private String nameVideo;

    public static VideoDTO from(Video video) {
        VideoDTO videoDTO = new VideoDTO();

        videoDTO.setVideoHash(video.getVideoHash());
        videoDTO.setNameVideo(video.getNameVideo());

        return videoDTO;
    }

}
