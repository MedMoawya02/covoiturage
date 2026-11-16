package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassagerRepository extends JpaRepository<Passager,Long> {
    Optional<Passager> findByEmail(String email);
    boolean existsByEmail(String email);
}
