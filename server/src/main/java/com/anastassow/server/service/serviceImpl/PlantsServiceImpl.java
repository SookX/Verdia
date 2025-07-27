package com.anastassow.server.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.dto.PlantPredictionResponse;
import com.anastassow.server.dto.PlantsDto;
import com.anastassow.server.jwt.JwtUtils;
import com.anastassow.server.mapper.PlantMapper;
import com.anastassow.server.models.Plants;
import com.anastassow.server.models.User;
import com.anastassow.server.repository.PlantsRepository;
import com.anastassow.server.repository.UserRepository;
import com.anastassow.server.service.PlantsService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PlantsServiceImpl implements PlantsService{
    
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PlantsRepository plantsRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public PlantsDto uploadPlant(MultipartFile file, String token) {
        
        Long userId = jwtUtils.getIdFromToken(token);
        User user = userRepo.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "spring_uploads")
            );

            String url = (String) uploadResult.get("secure_url");

            WebClient client = WebClient.create("http://localhost");

            // PlantPredictionResponse prediction = client.post()
            //         .uri("/ask")
            //         .bodyValue(Map.of("url", url))
            //         .retrieve()
            //         .bodyToMono(PlantPredictionResponse.class)
            //         .block();

            PlantPredictionResponse prediction = new PlantPredictionResponse(
                "Rose",
                "Infected",
                "100%",
                "Identify the infection by checking for black spots, powdery mildew, rust, or pests. Prune and remove affected leaves and stems. Clean the plant by rinsing with water. Apply appropriate fungicide or insecticide following instructions. Improve air circulation by trimming nearby plants. Water at the base, avoiding wetting the leaves. Maintain healthy soil with mulch and fertilizer"
            );

            Plants plant = Plants.builder()
                    .imageUrl(url)
                    .user(user)
                    .leafName(prediction.getLeafName())
                    .modelPrediction(prediction.getModelPrediction())
                    .howConfident(prediction.getHowConfident())
                    .description(prediction.getDescription())
                    .build();

            plantsRepo.save(plant);

            PlantsDto finalPlant = PlantMapper.plantMapperToDto(plant);
            return finalPlant;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public List<PlantsDto> getAllPlantsForUser(String token) {
        Long userId = jwtUtils.getIdFromToken(token);

        User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        List<Plants> plants = plantsRepo.findAllByUser(user);

        return plants.stream().map((plant) -> PlantMapper.plantMapperToDto(plant)).collect(Collectors.toList());
    }

    @Override
    public PlantsDto getPlantById(Long id, String token) {

        Plants plant = plantsRepo.findById(id).orElseThrow(() -> new RuntimeException("Plant not found"));

        if (plant.getUser().getId().equals(jwtUtils.getIdFromToken(token))) {
            return PlantMapper.plantMapperToDto(plant);
        } else throw new RuntimeException("Plant is not for the user");
    }
}
