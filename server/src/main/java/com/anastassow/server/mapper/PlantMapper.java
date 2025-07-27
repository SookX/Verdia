package com.anastassow.server.mapper;

import com.anastassow.server.dto.PlantsDto;
import com.anastassow.server.models.Plants;

public class PlantMapper {
    
    public static PlantsDto plantMapperToDto(Plants plant) {
        return new PlantsDto(
            plant.getId(),
            plant.getImageUrl(),
            plant.getUser().getId(),
            plant.getLeafName(),
            plant.getModelPrediction(),
            plant.getHowConfident(),
            plant.getDescription()
        );
    }
}
