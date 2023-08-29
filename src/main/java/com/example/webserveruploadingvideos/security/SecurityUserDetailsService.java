package com.example.webserveruploadingvideos.security;

import com.example.webserveruploadingvideos.exception.ItNotFoundException;
import com.example.webserveruploadingvideos.model.UserInfo;
import com.example.webserveruploadingvideos.repozitory.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class SecurityUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findById(username).orElseThrow(ItNotFoundException::new);

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                "",
                new ArrayList<>()
        );
    }
}
