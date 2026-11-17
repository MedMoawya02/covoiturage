package com.example.covoiturage.dto.response;

import com.example.covoiturage.enums.StatutTrajet;

import java.time.LocalDateTime;

public record TrajetResponse(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDateTime dateDepart,
        Integer placesProposees,
        Integer placesRestantes,
        StatutTrajet statut,
        String conducteurNomComplet,
        String vehiculeInfo
) {}