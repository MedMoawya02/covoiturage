package com.example.covoiturage.dto.response;

public record PassagerResponse(
        String id,
        String nom,
        String prenom,
        String email,
        String telephone
) {}
