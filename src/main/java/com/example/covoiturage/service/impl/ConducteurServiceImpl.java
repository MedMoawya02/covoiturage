package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.ConducteurRequest;
import com.example.covoiturage.dto.response.ConducteurResponse;
import com.example.covoiturage.entity.Conducteur;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.exception.ResourceNotFoundException;
import com.example.covoiturage.mapper.ConducteurMapper;
import com.example.covoiturage.repository.ConducteurRepository;
import com.example.covoiturage.repository.UserRepository;
import com.example.covoiturage.service.ConducteurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConducteurServiceImpl implements ConducteurService {
    private final ConducteurRepository conducteurRepository;
    private final UserRepository userRepository;
    private final ConducteurMapper conducteurMapper;
    @Override
    public Page<ConducteurResponse> findAll(Pageable pageable) {
        return conducteurRepository.findAll(pageable)
                .map(conducteurMapper::toResponse);
    }

    @Override
    public ConducteurResponse findById(Long id) {
        Conducteur conducteur=conducteurRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        return conducteurMapper.toResponse(conducteur);
    }

    @Override
    public Page<ConducteurResponse> search(String q, Pageable pageable) {
        if(q==null||q.isBlank()){
            return findAll(pageable);
        }
        return conducteurRepository.searchByNomOrEmail(q.trim(),pageable)
                .map(conducteurMapper::toResponse);
    }

    @Override
    @Transactional
    public ConducteurResponse update(Long id, ConducteurRequest request) {
        Conducteur conducteur=conducteurRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        //unicité d'email
        if(!conducteur.getEmail().equals(request.email())&&conducteurRepository.existsByEmail(request.email())){
            throw new BusinessRuleException("Email deja utilisé : "+request.email());
        }
        //unicité numer de permis
        if (!conducteur.getNumeroPermis().equals(request.numeroPermis())
                && conducteurRepository.existsByNumeroPermis(request.numeroPermis())) {
            throw new BusinessRuleException("Numéro de permis déjà utilisé");
        }
        conducteur.setNom(request.nom());
        conducteur.setPrenom(request.prenom());
        conducteur.setEmail(request.email());
        conducteur.setTelephone(request.telephone());
        conducteur.setNumeroPermis(request.numeroPermis());

        Conducteur saved=conducteurRepository.save(conducteur);
        return conducteurMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {

    }
}
