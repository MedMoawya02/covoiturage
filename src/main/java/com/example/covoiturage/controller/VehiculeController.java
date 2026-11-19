package com.example.covoiturage.controller;

import com.example.covoiturage.dto.request.VehiculeRequest;
import com.example.covoiturage.dto.response.VehiculeResponse;
import com.example.covoiturage.service.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehiculeController {
    private final VehiculeService vehiculeService;

    //créer vehicule pour un conducteur
    @PostMapping("/conducteur/{conducteurId}/vehicules")
    public ResponseEntity<VehiculeResponse> create(@PathVariable Long conducteurId, @Valid @RequestBody VehiculeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(conducteurId, request));
    }

    //détail d'un vehicule
    @GetMapping("/vehicules/{vehiculeId}")
    public ResponseEntity<VehiculeResponse> findVehicule(@PathVariable Long vehiculeId){
        return ResponseEntity.ok(vehiculeService.findById(vehiculeId));
    }

    //List des vehicules de conducteur
    @GetMapping("/conducteur/{conducteurId}/vehicules")
    public ResponseEntity<List<VehiculeResponse>> findVehiculeByConducteur(@PathVariable Long conducteurId){
        return ResponseEntity.ok(vehiculeService.findByConducteur(conducteurId));
    }

    //modifier un vehicule
    @PutMapping("/conducteurs/{conducteurId}/vehicules/{id}")
    public ResponseEntity<VehiculeResponse> update(@PathVariable Long conducteurId,Long vehiculeId,@Valid @RequestBody VehiculeRequest request){
        return ResponseEntity.ok(vehiculeService.update(conducteurId, vehiculeId, request));
    }

    //supprimer un vehicule
    @DeleteMapping("/conducteurs/{conducteurId}/vehicules/{vehiculeId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long conducteurId,
            @PathVariable Long vehiculeId) {
        vehiculeService.delete(conducteurId, vehiculeId);
        return ResponseEntity.noContent().build();
    }

}
