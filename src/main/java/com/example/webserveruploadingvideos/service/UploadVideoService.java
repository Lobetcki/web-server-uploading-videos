package com.example.webserveruploadingvideos.service;

import com.example.securitytoken.dto.LogIndication;
import com.example.securitytoken.dto.TokenDTO;
import com.example.securitytoken.model.Indication;
import com.example.securitytoken.model.SerialSecret;
import com.example.securitytoken.model.Token;
import com.example.securitytoken.repository.IndicationRepository;
import com.example.securitytoken.repository.SerialSecretRepository;
import com.example.securitytoken.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class UploadVideoService {

    private final IndicationRepository indicationRepository;
    private final SerialSecretRepository serialSecretRepository;
    private final TokenRepository tokenRepository;
    @Value("${telematika.security.token-expired}")
    private Integer expiredSec;

    public UploadVideoService(IndicationRepository indicationRepository,
                              SerialSecretRepository serialSecretRepository,
                              TokenRepository tokenRepository) {
        this.indicationRepository = indicationRepository;
        this.serialSecretRepository = serialSecretRepository;
        this.tokenRepository = tokenRepository;
    }


    public TokenDTO authority(SerialSecret serialSecret) {
        if (serialSecretRepository.existsBySecretAndSerial(
                serialSecret.getSecret(), serialSecret.getSerial())) {
            UUID uuid = UUID.randomUUID();
            Token token = new Token();
            token.setUuid(String.valueOf(uuid));
            token.setExpiredData(Instant.now().plus(expiredSec, ChronoUnit.SECONDS));
            token.setSerialSecret(serialSecret);
            tokenRepository.save(token);

            return TokenDTO.fromToken(token);
        }
        return null;
    }

    public Double calculateAvgIndication(String serial) {
        return indicationRepository.getAvgIndication(serial);
    }

    public void save(SerialSecret serialSecret, LogIndication logIndication) {
        Indication indication = new Indication();
        indication.setValue(logIndication.getValue());
        indication.setSerialSecret(serialSecret);

        indicationRepository.save(indication);
    }
}
