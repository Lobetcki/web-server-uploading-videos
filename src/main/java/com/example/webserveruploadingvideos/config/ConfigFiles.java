package com.example.webserveruploadingvideos.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

@Service
public class ConfigFiles {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(100)); // Новый максимальный размер файла
        factory.setMaxRequestSize(DataSize.ofMegabytes(100)); // Максимальный размер всех файлов в одном запросе
        return factory.createMultipartConfig();
    }

}
