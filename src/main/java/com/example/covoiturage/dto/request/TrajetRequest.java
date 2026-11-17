package com.example.covoiturage.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetRequest(
        @NotNull Long vehiculeId,
        @NotNull LocalDateTime dateDepart,
        @NotNull @Min(1) Integer placesProposees,
        @NotNull @Size(min = 2, message = "Un trajet doit avoir au moins 2 points d'arrêt")
        @Valid List<PointArretRequest> pointsArret
) {}