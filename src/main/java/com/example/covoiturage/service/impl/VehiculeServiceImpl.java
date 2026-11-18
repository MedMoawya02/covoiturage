package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.VehiculeRequest;
import com.example.covoiturage.dto.response.VehiculeResponse;
import com.example.covoiturage.entity.Conducteur;
import com.example.covoiturage.entity.Vehicule;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.exception.ForbiddenException;
import com.example.covoiturage.exception.ResourceNotFoundException;
import com.example.covoiturage.mapper.VehiculeMapper;
import com.example.covoiturage.repository.ConducteurRepository;
import com.example.covoiturage.repository.TrajetRepository;
import com.example.covoiturage.repository.VehiculeRepository;
import com.example.covoiturage.service.VehiculeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final VehiculeMapper vehiculeMapper;
    @Override
    public VehiculeResponse create(Long conducteurId, VehiculeRequest request) {
        Conducteur conducteur=conducteurRepository.findById(conducteurId).orElseThrow(()->new ResourceNotFoundException("Conducteur introuvable : "+conducteurId));
        //imma unique
        if (vehiculeRepository.existsByImmatriculation(request.immatriculation())){
            throw new BusinessRuleException("Immatriculation déjà utilisée : " + request.immatriculation());
        }
        //verification capacité
        if(request.capacite()<1||request.capacite()>8){
            throw new BusinessRuleException("La capacité doit être comprise entre 1 et 8");
        }
        Vehicule vehicule=vehiculeMapper.toEntity(request);
        vehicule.setConducteur(conducteur);
        Vehicule saved=vehiculeRepository.save(vehicule);
        return vehiculeMapper.toResponse(saved);
    }

    @Override
    public VehiculeResponse findById(Long id) {
        Vehicule vehicule=vehiculeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    public List<VehiculeResponse> findByConducteur(Long conducteurId) {
        if(!conducteurRepository.existsById(conducteurId)){
            throw new ResourceNotFoundException("Conducteur introuvable : " + conducteurId);
        }
        return vehiculeRepository.findByConducteurId(conducteurId)
                .stream()
                .map(vehiculeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehiculeResponse update(Long conducteurId, Long vehiculeId, VehiculeRequest request) {
        Vehicule vehicule=vehiculeRepository.findById(vehiculeId).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+vehiculeId));
        //seul la propritere peut modifier
        if(!vehicule.getConducteur().getId().equals(conducteurId)){
            throw new BusinessRuleException("Seule la propriétére peut modifier ses vehicules");
        }
        //immatruculation unique c'est changer
        if (!vehicule.getImmatriculation().equals(request.immatriculation())&&vehiculeRepository.existsByImmatriculation(request.immatriculation())){
            throw new BusinessRuleException("Immatriculation déjà utilisée : " + request.immatriculation());
        }
        if (request.capacite() < 1 || request.capacite() > 8) {
            throw new BusinessRuleException("La capacité doit être comprise entre 1 et 8");
        }

        vehicule.setMarque(request.marque());
        vehicule.setModele(request.modele());
        vehicule.setImmatriculation(request.immatriculation());
        vehicule.setCapacite(request.capacite());

        Vehicule saved=vehiculeRepository.save(vehicule);
        return vehiculeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long conducteurId, Long vehiculeId) {
        Vehicule vehicule=vehiculeRepository.findById(vehiculeId).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+vehiculeId));
        if(!vehicule.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Ce véhicule n'appartient pas à ce conducteur");
        }
        //pas de suppression si y'a des trajets
        Boolean Atrajet=trajetRepository.existsByVehiculeId(vehiculeId);
        if(Atrajet){
            throw new BusinessRuleException(
                    "Impossible de supprimer un véhicule associé à un trajet");
        }
        vehiculeRepository.delete(vehicule);
    }
}
