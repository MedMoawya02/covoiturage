package com.example.covoiturage.repository;

import com.example.covoiturage.entity.PointArret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointArretRepository extends JpaRepository<PointArret, Long> {
}