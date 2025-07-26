package com.anastassow.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anastassow.server.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);
}
