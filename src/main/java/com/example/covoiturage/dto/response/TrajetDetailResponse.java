package com.example.covoiturage.dto.response;

import com.example.covoiturage.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetDetailResponse(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDateTime dateDepart,
        Integer placesProposees,
        Integer placesRestantes,
        StatutTrajet statut,
        ConducteurResponse conducteur,
        VehiculeResponse vehicule,
        List<PointArretResponse> pointsArret
) {}