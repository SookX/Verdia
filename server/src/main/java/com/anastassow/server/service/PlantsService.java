package com.anastassow.server.service;

import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.dto.PlantsDto;
import com.anastassow.server.models.Plants;

public interface PlantsService {
    public PlantsDto uploadPlant(MultipartFile file, String token);
}
