package com.example.covoiturage.dto.response;

import com.example.covoiturage.enums.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long trajetId,
        String villeDepart,
        String villeArrivee,
        String pointMonteeVille,
        String pointDescenteVille,
        Integer nombrePlaces,
        BigDecimal montantTotal,
        LocalDateTime dateReservation,
        StatutReservation statut
) {}