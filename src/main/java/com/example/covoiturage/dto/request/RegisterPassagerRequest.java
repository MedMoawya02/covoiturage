package com.example.covoiturage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterPassagerRequest(
        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank @Email String email,
        @NotBlank String telephone,
        @NotBlank @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String password
) {}