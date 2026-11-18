package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.ConducteurRequest;
import com.example.covoiturage.dto.response.ConducteurResponse;
import com.example.covoiturage.entity.Conducteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConducteurService {
    Page<ConducteurResponse> findAll(Pageable pageable);
    ConducteurResponse findById(Long id);
    //recherche par nom,prenom ou email
    Page<ConducteurResponse> search(String q,Pageable pageable);
    //Modifier conducteur
    ConducteurResponse update(Long id, ConducteurRequest request);
    //Supprimer conducteur
    void delete(Long id);
}
