package com.anastassow.server.service.serviceImpl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anastassow.server.jwt.JwtUtils;
import com.anastassow.server.dto.AuthRequest;
import com.anastassow.server.dto.AuthResponse;
import com.anastassow.server.dto.UserDto;
import com.anastassow.server.models.User;
import com.anastassow.server.repository.UserRepository;
import com.anastassow.server.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private HttpServletRequest request;

    private final GoogleIdTokenVerifier verifier;

    public UserServiceImpl(GoogleIdTokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public AuthResponse register(AuthRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getUsername());
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail());

            return new AuthResponse(token, user.getEmail(), user.getUsername());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public UserDto user(String token) {
        return new UserDto(jwtUtil.getIdFromToken(token), jwtUtil.getUsernameFromToken(token), jwtUtil.getEmailFromToken(token));
    }

    @Override
    public String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                return null;
            }
            return idToken.getPayload();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
