package com.example.webserveruploadingvideos.repozitory;

import com.example.webserveruploadingvideos.enums.StatusVideo;
import com.example.webserveruploadingvideos.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    List<Video> findAllByStatus(StatusVideo statusVideo);
}
