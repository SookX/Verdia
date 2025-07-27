package com.anastassow.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.dto.PlantsDto;
import com.anastassow.server.service.PlantsService;
import com.anastassow.server.service.UserService;

@RestController
@RequestMapping("/plant")
public class PlantsController {
    
    @Autowired
    private PlantsService plantsService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        String token = userService.extractToken(authHeader);
        
        try {
            PlantsDto plant = plantsService.uploadPlant(file, token);
            return ResponseEntity.ok(plant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Image upload failed: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPlants(@RequestHeader("Authorization") String authHeader) {
        String token = userService.extractToken(authHeader);

        List<PlantsDto> plants = plantsService.getAllPlantsForUser(token);

        return ResponseEntity.ok(plants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlantById(@PathVariable("id") Long plantId, @RequestHeader("Authorization") String authHeader) {
        String token = userService.extractToken(authHeader);
        
        try{
            PlantsDto plant = plantsService.getPlantById(plantId, token);
            return ResponseEntity.ok(plant);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
