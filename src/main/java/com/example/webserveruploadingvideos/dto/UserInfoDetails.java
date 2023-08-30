package com.example.webserveruploadingvideos.dto;

import com.example.webserveruploadingvideos.enums.Role;
import com.example.webserveruploadingvideos.model.UserInfo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserInfoDetails implements UserDetails {

    private String userName;
    private Role role;

    private List<GrantedAuthority> authorities;

    public static UserInfoDetails fromUserInfoDetails(UserInfo userInfo) {
        UserInfoDetails userInfoDetails = new UserInfoDetails();
        userInfoDetails.setUserName(userInfo.getUserName());
        userInfoDetails.setRole(userInfo.getRole());
        return userInfoDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
//        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
