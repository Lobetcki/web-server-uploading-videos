package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.service.UploadVideoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/indications")
public class UploadVideoController {

    private final UploadVideoService uploadVideoService;

    public UploadVideoController(UploadVideoService uploadVideoService) {
        this.uploadVideoService = uploadVideoService;
    }

    @PostMapping("/authority")
    public TokenDTO authority(@RequestBody SerialSecret serialSecret){
        return uploadVideoService.authority(serialSecret);
    }

    @PostMapping
    public void indication(@RequestBody LogIndication logIndication, Authentication authentication) {
        Token principal = (Token) authentication.getPrincipal();
        uploadVideoService.save(principal.getSerialSecret(), logIndication);
    }

    @GetMapping("/avg/{serial}")
    public Double avg(@PathVariable String serial) {
        return uploadVideoService.calculateAvgIndication(serial);
    }


}
