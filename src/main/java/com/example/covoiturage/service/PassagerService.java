package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.PassagerRequest;
import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.dto.response.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PassagerService {

    Page<PassagerResponse> findAll(Pageable pageable);

    PassagerResponse findById(Long id);

    Page<PassagerResponse> search(String q, Pageable pageable);

    PassagerResponse update(Long id, PassagerRequest request);

    void delete(Long id);

    //historique des réservations d'un passager
    List<ReservationResponse> findReservations(Long passagerId);
}