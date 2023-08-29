package com.example.webserveruploadingvideos.repozitory;

import com.example.webserveruploadingvideos.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

    @Query("SELECT u FROM UserInfo u JOIN FETCH u.downloadableVideo WHERE u.userName = :name")
    Optional<UserInfo> findByUserNameWithVideos(@Param("name") String name);

}
