package com.anastassow.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantsDto {
    private Long id;
    private String imageUrl;
    private Long userId;
}
