
package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.*;
import com.example.covoiturage.dto.response.AuthResponse;
import com.example.covoiturage.entity.*;
import com.example.covoiturage.enums.Role;
import com.example.covoiturage.mapper.ConducteurMapper;
import com.example.covoiturage.mapper.PassagerMapper;
import com.example.covoiturage.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private PassagerRepository passagerRepository;

    @Mock
    private ConducteurMapper conducteurMapper;

    @Mock
    private PassagerMapper passagerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerConducteur_Success() {

        // 1. ARRANGE : préparer les données
        RegisterConducteurRequest request =
                new RegisterConducteurRequest(
                        "Moawya",
                        "Mohamed",
                        "conducteur@test.com",
                        "123456",
                        "123456",
                        "A2321"
                );

        Conducteur conducteur = new Conducteur();
        conducteur.setId(1L);
        conducteur.setNom("Moawya");
        conducteur.setPrenom("Mohamed");
        conducteur.setEmail("conducteur@test.com");
        conducteur.setNumeroPermis("A2321");

        // Simuler les réponses des dépendances
        when(conducteurRepository.existsByEmail("conducteur@test.com"))
                .thenReturn(false);

        when(conducteurRepository.existsByNumeroPermis("A2321"))
                .thenReturn(false);

        when(conducteurMapper.toEntity(request))
                .thenReturn(conducteur);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(conducteurRepository.save(conducteur))
                .thenReturn(conducteur);

        // 2. ACT : exécuter la méthode
        AuthResponse response = authService.registerConducteur(request);

        // 3. ASSERT : vérifier le résultat
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("conducteur@test.com", response.email());
        assertEquals(Role.CONDUCTEUR, response.role());
        assertEquals("/conducteur", response.redirectUrl());

        // Vérifier que le mot de passe a été encodé
        assertEquals("encodedPassword", conducteur.getPassword());

        // Vérifier les interactions avec les dépendances
        verify(conducteurRepository).existsByEmail("conducteur@test.com");
        verify(conducteurRepository).existsByNumeroPermis("A2321");
        verify(conducteurMapper).toEntity(request);
        verify(passwordEncoder).encode("123456");
        verify(conducteurRepository).save(conducteur);
    }
}
