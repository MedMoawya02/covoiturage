package com.example.covoiturage.repository;

import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.Trajet;
import com.example.covoiturage.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet,Long> {
    boolean existsByVehiculeId(Long vehiculeId);
    List<Trajet> findByConducteurId(Long conducteurId);

    //trajet avec details
    @Query("""
        SELECT DISTINCT t FROM Trajet t
        JOIN FETCH t.pointsArret
        JOIN FETCH t.conducteur
        JOIN FETCH t.vehicule
        WHERE t.id=:id
""")
    Optional<Trajet> findByIdWithDetails(@Param("id") Long id);

    //chercher un trajet
    @Query("""
        SELECT t FROM Trajet t
        JOIN FETCH t.conducteur c
        JOIN FETCH t.vehicule v 
        JOIN t.pointsArret pm 
        JOIN t.pointsArret pa
        WHERE pm.ville=:villeDepart
        AND pa.ville=:villeArrivee
        AND pm.ordre<pa.ordre
        AND t.statut=:statut
        AND t.dateDepart>=:dateMin 
         AND (:prixMax IS NULL OR (pa.prixDepuisDepart - pm.prixDepuisDepart) <= :prixMax)
""")
    Page<Trajet> rechercherTrajets(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("dateMin")LocalDateTime dateMin,
            @Param("statut")StatutTrajet statut,
            @Param("prixMax")BigDecimal prixMax,
            Pageable pageable
            );
}
