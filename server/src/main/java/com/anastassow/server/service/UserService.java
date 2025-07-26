package com.anastassow.server.service;

import com.anastassow.server.dto.AuthRequest;
import com.anastassow.server.dto.AuthResponse;
import com.anastassow.server.dto.UserDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface UserService {
    AuthResponse register(AuthRequest request); 
    AuthResponse login(AuthRequest request);
    UserDto user(String token);
    String extractToken(String authHeader);
    GoogleIdToken.Payload verify(String idTokenString);
}
