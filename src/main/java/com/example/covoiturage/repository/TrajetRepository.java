package com.example.covoiturage.repository;

import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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
        WHERE t.Id=:id
""")
    Optional<Trajet> findByIdWithDetails(@Param("id") Long id);
}
