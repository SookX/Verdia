package com.anastassow.server.service;

import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.models.Plants;

public interface PlantsService {
    public Plants uploadPlant(MultipartFile file);
}
