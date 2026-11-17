package com.example.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointArretRequest(
        @NotBlank String ville,
        @NotNull @Min(0) Integer ordre,
        @NotNull LocalDateTime heurePassage,
        @NotNull @PositiveOrZero BigDecimal prixDepuisDepart
) {}