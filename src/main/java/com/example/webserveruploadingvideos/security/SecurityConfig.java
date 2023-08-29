package com.example.webserveruploadingvideos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // Создаем бин для хранения пользователей в памяти приложения
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/user/**"))
                .hasRole("USER")
                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .and()
                .httpBasic();

        return http.build();
    }
}