package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.ReservationRequest;
import com.example.covoiturage.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {
    //rséerver
    ReservationResponse reserver(Long passagerId, ReservationRequest request);

    //détail d'un reservations
    ReservationResponse findById(Long reservationId);

    //historique des réservation d'un passager
    List<ReservationResponse> findByPassager(Long passagerId);

    //annuler une réservation
    void annuler(Long passagerId,Long reservationId);
}
