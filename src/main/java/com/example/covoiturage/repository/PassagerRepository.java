package com.example.covoiturage.repository;

import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.entity.Passager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassagerRepository extends JpaRepository<Passager,Long> {
    Optional<Passager> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
        SELECT p FROM Passager p
        WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%',:q,'%') )
            OR LOWER(p.prenom) LIKE LOWER(CONCAT('%',:q,'%') )
            OR LOWER(p.email) LIKE LOWER(CONCAT('%',:q,'%') ) 
""")
    Page<Passager> searchByNomOrEmail(@Param("q") String q, Pageable pageable);
}
