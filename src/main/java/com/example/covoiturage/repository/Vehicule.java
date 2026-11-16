package com.example.covoiturage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Vehicule extends JpaRepository<Vehicule,Long> {
    List<Vehicule> findByConducteurId(Long id);
    boolean existsByImmatriculation(String immatriculation);
}
