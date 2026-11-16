package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur,Long> {
    Optional<Conducteur> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNumeroPermis(String numeroPermis);
}
