package com.anastassow.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anastassow.server.models.Plants;
import com.anastassow.server.models.User;

public interface PlantsRepository extends JpaRepository<Plants, Long> {
    List<Plants> findAllByUser(User user);
}
