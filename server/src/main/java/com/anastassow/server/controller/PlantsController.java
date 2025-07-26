package com.anastassow.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.models.Plants;
import com.anastassow.server.service.PlantsService;
import com.anastassow.server.service.serviceImpl.PlantsServiceImpl;

@RestController
@RequestMapping("/plant")
public class PlantsController {
    
    @Autowired
    private PlantsService plantsService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Plants image = plantsService.uploadPlant(file);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Image upload failed: " + e.getMessage());
        }
    }
}
