package com.example.covoiturage.dto.response;

import com.example.covoiturage.enums.Role;

public record AuthResponse(
        Long id,
        String nom,
        String prenom,
        String email,
        Role role,
        String redirectUrl
) {}