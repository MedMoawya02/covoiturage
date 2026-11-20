package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByPassagerId(Long id);
    List<Reservation> findByTrajetId(Long id);

    @Query("""
    SELECT COALESCE(SUM (r.nombrePlaces))FROM Reservation r 
    WHERE r.trajet.id=:trajetId
    AND r.statut=:statut
""")
    int sumPlacesReserves(
            @Param("trajetId") Long trajetId,
            @Param("statut")StatutReservation staut
            );
}
