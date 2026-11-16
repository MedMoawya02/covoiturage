package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByPassagerId(Long id);
    List<Reservation> findByTrajetId(Long id);
}
