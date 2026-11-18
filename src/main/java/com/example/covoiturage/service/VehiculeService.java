package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.VehiculeRequest;
import com.example.covoiturage.dto.response.VehiculeResponse;
import com.example.covoiturage.entity.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehiculeService {
    //l'ajout d'un vehicule à un conducteur
    VehiculeResponse create(Long conducteurId, VehiculeRequest request);

    VehiculeResponse findById(Long id);

    //liste des vehicules d'un condctuer
    List<VehiculeResponse> findByConducteur(Long conducteurId);

    //modifier vehicule
    VehiculeResponse update(Long conducteurId,Long vehiculeId,VehiculeRequest request);

    //supprimer vehicule
    void delete(Long conducteurId,Long vehiculeId);
}
