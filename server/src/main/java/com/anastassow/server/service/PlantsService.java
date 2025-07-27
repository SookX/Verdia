package com.anastassow.server.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.dto.PlantsDto;

public interface PlantsService {
    public PlantsDto uploadPlant(MultipartFile file, String token);
    public List<PlantsDto> getAllPlantsForUser(String token);
    public PlantsDto getPlantById(Long id, String token);
}
