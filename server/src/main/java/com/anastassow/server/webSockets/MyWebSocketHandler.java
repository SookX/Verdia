package com.anastassow.server.webSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.anastassow.server.dto.PlantsDto;
import com.anastassow.server.mapper.PlantMapper;
import com.anastassow.server.models.Plants;
import com.anastassow.server.models.User;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        User user = (User) session.getAttributes().get("user");
        Plants plant = (Plants) session.getAttributes().get("plant");

        String roomId = generateRoomKey(user.getId(), plant.getId());
        userRooms.put(roomId, session);

        session.sendMessage(new TextMessage("Connected to room for plant ID: " + plant.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        User user = (User) session.getAttributes().get("user");
        Plants plant = (Plants) session.getAttributes().get("plant");
        String question = message.getPayload();

        String response = simulateFastApiResponse(user, plant, question);
        session.sendMessage(new TextMessage("Answer: " + response));
    }

    private String askMicroservice(User user, Plants plant, String question) {
        PlantsDto plantDto = PlantMapper.plantMapperToDto(plant);

        Map<String, Object> requestBody = Map.of(
            "plant", plantDto,
            "question", question,
            "userId", user.getId()
        );

        try {

            WebClient client = WebClient.create("");

            return client.post()
                    .uri("http://localhost:8000/ask")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "Error contacting AI service: " + e.getMessage();
        }
    }

    private String simulateFastApiResponse(User user, Plants plant, String question) {
        return """
            {
                For fungal infections like black spot, you may see improvement in 5 to 7 days, with full recovery in 2 to 4 weeks if treated properly. Powdery mildew may improve in 3 to 5 days and fully heal in about 2 to 3 weeks. Pest infestations, such as aphids, can show improvement within 2 to 3 days and may be resolved in 1 to 2 weeks. Rust infections usually start improving in about a week and may take 3 to 4 weeks to fully clear.Recovery can be sped up by pruning affected leaves, applying the appropriate fungicide or insecticide, improving air circulation, watering at the base of the plant, and keeping the soil healthy with mulch and nutrients.
            }
        """;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        User user = (User) session.getAttributes().get("user");
        Plants plant = (Plants) session.getAttributes().get("plant");
        userRooms.remove(generateRoomKey(user.getId(), plant.getId()));
    }

    private String generateRoomKey(Long userId, Long plantId) {
        return "room:" + userId + ":" + plantId;
    }
}
