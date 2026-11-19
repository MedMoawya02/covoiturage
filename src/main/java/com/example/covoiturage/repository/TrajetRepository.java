package com.example.covoiturage.repository;

import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet,Long> {
    boolean existsByVehiculeId(Long vehiculeId);
    List<Trajet> findByConducteurId(Long conducteurId);
}
