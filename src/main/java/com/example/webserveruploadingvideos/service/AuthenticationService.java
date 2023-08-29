package com.example.webserveruploadingvideos.service;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

//    private final TokenRepository tokenRepository;
//
//    public AuthenticationService(TokenRepository tokenRepository) {
//        this.tokenRepository = tokenRepository;
//    }
//
//    public Optional<Token> findByUuid(String uuid) {
//        Optional<Token> token = tokenRepository.findById(uuid);
//        if ( token.isPresent() && Instant.now().isBefore(token.get().getExpiredData())) {
//            return token;
//        }
//        return Optional.empty();
//    }
}
