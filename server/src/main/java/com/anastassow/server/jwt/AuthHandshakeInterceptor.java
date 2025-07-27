package com.anastassow.server.jwt;

import com.anastassow.server.models.Plants;
import com.anastassow.server.models.User;
import com.anastassow.server.repository.PlantsRepository;
import com.anastassow.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlantsRepository plantRepo;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            URI uri = request.getURI();
            String query = uri.getQuery(); // token=xxx&plantId=xxx

            if (query == null) return false;

            Map<String, String> params = Arrays.stream(query.split("&"))
                    .map(p -> p.split("="))
                    .filter(p -> p.length == 2)
                    .collect(Collectors.toMap(p -> p[0], p -> p[1]));

            String token = params.get("token");
            String plantIdStr = params.get("plantId");

            if (token == null || plantIdStr == null) return false;

            Long userId = jwtUtils.getIdFromToken(token);
            User user = userRepo.findById(userId).orElse(null);
            if (user == null) return false;

            Long plantId = Long.parseLong(plantIdStr);
            Plants plant = plantRepo.findById(plantId).orElse(null);

            if (plant == null || !plant.getUser().getId().equals(userId)) return false;

            attributes.put("user", user);
            attributes.put("plant", plant);
            return true;
        } catch (Exception e) {
            System.out.println("Handshake failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    
    }
}
