package com.example.covoiturage.repository;

import com.example.covoiturage.entity.Conducteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur,Long> {
    Optional<Conducteur> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNumeroPermis(String numeroPermis);
    @Query("""
        SELECT c FROM Conducteur c
        WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%',:q,'%') )
            OR LOWER(c.prenom) LIKE LOWER(CONCAT('%', :q, '%'))
                       OR LOWER(c.email) LIKE LOWER(CONCAT('%', :q, '%'))
""")
    Page<Conducteur> searchByNomOrEmail(@Param("q") String q, Pageable pageable);
}
