package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.LoginRequest;
import com.example.covoiturage.dto.request.RegisterConducteurRequest;
import com.example.covoiturage.dto.request.RegisterPassagerRequest;
import com.example.covoiturage.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerConducteur(RegisterConducteurRequest request);
    AuthResponse registerPassager(RegisterPassagerRequest request);
    AuthResponse login(LoginRequest request);
}
