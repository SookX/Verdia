package com.anastassow.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantPredictionResponse {
    private String leafName;
    private String modelPrediction;
    private String howConfident;
    private String description;
}
