package com.anastassow.server.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.anastassow.server.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                ServerHttpResponse response,
                                WebSocketHandler wsHandler,
                                Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String token = httpRequest.getParameter("token");
            if (token == null || token.isEmpty()) {
                System.out.println("Missing token, rejecting WS connection");
                return false;
            }

            if (!jwtUtils.validate(token)) {
                System.out.println("Invalid token, rejecting WS connection");
                return false;
            }

            String email = jwtUtils.getEmailFromToken(token);
            var userDetails = customUserDetailsService.loadUserByUsername(email);
            if (userDetails == null) {
                System.out.println("User not found, rejecting WS connection");
                return false;
            }

            attributes.put("user", userDetails);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
