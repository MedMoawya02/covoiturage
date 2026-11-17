package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.LoginRequest;
import com.example.covoiturage.dto.request.RegisterConducteurRequest;
import com.example.covoiturage.dto.request.RegisterPassagerRequest;
import com.example.covoiturage.dto.response.AuthResponse;
import com.example.covoiturage.entity.Conducteur;
import com.example.covoiturage.entity.Passager;
import com.example.covoiturage.entity.User;
import com.example.covoiturage.enums.Role;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.mapper.ConducteurMapper;
import com.example.covoiturage.mapper.PassagerMapper;
import com.example.covoiturage.repository.ConducteurRepository;
import com.example.covoiturage.repository.PassagerRepository;
import com.example.covoiturage.repository.UserRepository;
import com.example.covoiturage.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ConducteurRepository conducteurRepository;
    private final PassagerRepository passagerRepository;
    private final ConducteurMapper conducteurMapper;
    private final PassagerMapper passagerMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public AuthResponse registerConducteur(RegisterConducteurRequest request) {
        if(conducteurRepository.existsByEmail(request.email())){
            throw new BusinessRuleException("Email already exists");
        }
        if(conducteurRepository.existsByNumeroPermis(request.numeroPermis())){
            throw new BusinessRuleException("Numero de permis deja exist");
        }
        Conducteur conducteur=conducteurMapper.toEntity(request);
        conducteur.setPassword(passwordEncoder.encode(request.password()));
        Conducteur saved=conducteurRepository.save(conducteur);
        return new AuthResponse(
                saved.getId(), saved.getNom(), saved.getPrenom(),
                saved.getEmail(), Role.CONDUCTEUR, "/conducteur"
        );
    }

    @Override
    @Transactional
    public AuthResponse registerPassager(RegisterPassagerRequest request) {
        if(passagerRepository.existsByEmail(request.email())){
            throw new BusinessRuleException("Email already exist");
        }
        Passager passager=passagerMapper.toEntity(request);
        passager.setPassword(passwordEncoder.encode(request.password()));
        Passager saved=passagerRepository.save(passager);
        return new AuthResponse(
                saved.getId(),saved.getNom(), saved.getPrenom(), saved.getEmail(),Role.PASSAGER,"/passager"
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user=userRepository.findByEmail(request.email()).orElseThrow(()->new BusinessRuleException("Email introuvable"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessRuleException("Mot de passe incorrect");
        }
        String redirictUrl=switch (user.getRole()){
            case CONDUCTEUR -> "/conducteur";
            case PASSAGER -> "/passager";
            case ADMIN -> "/admin";
        };
        return new AuthResponse(
                user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), user.getRole(),redirictUrl
        );
    }
}
