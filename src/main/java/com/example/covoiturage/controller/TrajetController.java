package com.example.covoiturage.controller;

import com.example.covoiturage.dto.request.TrajetRequest;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.service.TrajetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrajetController {
    private final TrajetService trajetService;

    @PostMapping("/conducteurs/{conducteurId}/trajets")
    public ResponseEntity<TrajetDetailResponse> publier(@PathVariable Long conducteurId, @Valid @RequestBody TrajetRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(trajetService.publier(conducteurId, request));
    }
    //detail d'un trajet
    @GetMapping("/trajets/{id}")
    public ResponseEntity<TrajetDetailResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(trajetService.findById(id));
    }
    //liste des trajets d'un conducteurs
    @GetMapping("/conducteurs/{conducteurId}/trajets")
    public ResponseEntity<List<TrajetResponse>> findByConducteur(@PathVariable Long conducteurId){
        return ResponseEntity.ok(trajetService.findByConducteur(conducteurId));
    }
    //résérvations d'un trajet pour un conducteur
    @GetMapping("/conducteurs/{conducteurId}/trajets/{trajetId}/reservations")
    public ResponseEntity<List<ReservationResponse>> findReservations(@PathVariable Long conducteurId,@PathVariable Long trajetId){
        return ResponseEntity.ok(trajetService.findReservations(conducteurId, trajetId));
    }
    //terminer un traje
    @PatchMapping("/conducteurs/{conducteurId}/trajets/{trajetId}/terminer")
    public ResponseEntity<TrajetDetailResponse> terminer(
            @PathVariable Long conducteurId,
            @PathVariable Long trajetId) {
        return ResponseEntity.ok(trajetService.terminer(conducteurId, trajetId));
    }

    //annuler un trajet
    @PatchMapping("/conducteurs/{conducteurId}/trajets/{trajetId}/annuler")
    public ResponseEntity<Void> annuler(
            @PathVariable Long conducteurId,
            @PathVariable Long trajetId) {
        trajetService.annuler(conducteurId, trajetId);
        return ResponseEntity.noContent().build();
    }

}
