package com.example.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        @NotNull Long trajetId,
        @NotNull Long pointMonteeId,
        @NotNull Long pointDescenteId,
        @NotNull @Min(1) Integer nombrePlaces
) {}