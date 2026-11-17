package com.example.covoiturage.controller;

import com.example.covoiturage.dto.request.LoginRequest;
import com.example.covoiturage.dto.request.RegisterConducteurRequest;
import com.example.covoiturage.dto.request.RegisterPassagerRequest;
import com.example.covoiturage.dto.response.AuthResponse;
import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/conducteur")
    public ResponseEntity<AuthResponse> registerConducteur(@Valid @RequestBody RegisterConducteurRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerConducteur(request));
    }
    //registre passager
    @PostMapping("/register/passager")
    public ResponseEntity<AuthResponse> registerPassager(@Valid @RequestBody RegisterPassagerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerPassager(request));
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
