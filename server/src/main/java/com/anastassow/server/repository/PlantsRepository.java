package com.anastassow.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anastassow.server.models.Plants;

public interface PlantsRepository extends JpaRepository<Plants, Long> {
}
