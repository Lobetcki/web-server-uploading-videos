package com.example.webserveruploadingvideos.repozitory;

import com.example.webserveruploadingvideos.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

}
