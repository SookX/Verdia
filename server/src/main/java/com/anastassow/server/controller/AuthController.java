package com.anastassow.server.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anastassow.server.dto.AuthRequest;
import com.anastassow.server.dto.AuthResponse;
import com.anastassow.server.dto.TokenRequest;
import com.anastassow.server.dto.UserDto;
import com.anastassow.server.jwt.JwtUtils;
import com.anastassow.server.models.User;
import com.anastassow.server.repository.UserRepository;
import com.anastassow.server.service.UserService;
import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> user(@RequestHeader("Authorization") String authHeader) {
        String token = userService.extractToken(authHeader);
        UserDto user = userService.user(token);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@RequestBody TokenRequest tokenRequest) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = userService.verify(tokenRequest.getToken());

        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = payload.getEmail();
        String username = (String) payload.get("name");

        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);
            return userRepo.save(newUser);
        });;

        String token = jwtUtils.generateToken(null, username, email);

        AuthResponse response = new AuthResponse(token, email, username);

        return ResponseEntity.ok(response);
    }
}
