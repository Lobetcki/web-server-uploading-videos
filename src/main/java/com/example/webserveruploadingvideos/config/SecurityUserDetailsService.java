package com.example.webserveruploadingvideos.config;

import com.example.webserveruploadingvideos.dto.UserInfoDetails;
import com.example.webserveruploadingvideos.exception.ItNotFoundException;
import com.example.webserveruploadingvideos.model.UserInfo;
import com.example.webserveruploadingvideos.repozitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {

            UserInfo userInfo = userRepository.findById(username)
                .orElseThrow(ItNotFoundException::new);

        return UserInfoDetails.fromUserInfoDetails(userInfo);
    }
}
