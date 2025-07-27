package com.anastassow.server.webSockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.anastassow.server.models.User;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        User user = (User) session.getAttributes().get("user");
        String plant = (String) session.getAttributes().get("plant");

        String roomId = generateRoomKey(user.getId(), plant);
        userRooms.put(roomId, session);

        session.sendMessage(new TextMessage("Connected to room: " + plant));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        User user = (User) session.getAttributes().get("user");
        String plant = (String) session.getAttributes().get("plant");
        String question = message.getPayload();

        String response = askMicroservice(user.getId(), plant, question);

        session.sendMessage(new TextMessage("Answer: " + response));
    }

    private String askMicroservice(Long userId, String plant, String question) {
        // WebClient client = WebClient.create("http://localhost");

        // return client.post()
        //         .uri("/ask")
        //         .bodyValue(Map.of("userId", userId, "plant", plant, "question", question))
        //         .retrieve()
        //         .bodyToMono(String.class)
        //         .block();

        return "Model in progress!";
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        User user = (User) session.getAttributes().get("user");
        String plant = (String) session.getAttributes().get("plant");
        userRooms.remove(generateRoomKey(user.getId(), plant));
    }

    private String generateRoomKey(Long userId, String plant) {
        return "room:" + userId + ":" + plant.toLowerCase();
    }
}
