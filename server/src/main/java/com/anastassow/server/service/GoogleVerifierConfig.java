package com.anastassow.server.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;

@Configuration
public class GoogleVerifierConfig {

    @Value("${CLIENT_ID_GOOGLE}")
    private String clientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        System.out.println("Creating GoogleIdTokenVerifier with clientId: " + clientId);
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), Utils.getDefaultJsonFactory())
            .setAudience(Collections.singletonList(clientId))
            .build();
    }
}
