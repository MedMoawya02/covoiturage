package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet,Long> {
}
