package com.example.covoiturage.service;

import com.example.covoiturage.dto.request.TrajetRequest;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.Trajet;

import java.util.List;

public interface TrajetService {
    //Publier un trajet
    TrajetDetailResponse publier(Long conducteurId, TrajetRequest request);

    //détail d'un trajet
    TrajetDetailResponse findById(Long id);

    //Liste des trajets d'un conducteur
    List<TrajetResponse> findByConducteur(Long conducteurId);

    //Réservation d'un trajet d'un conducteur
    List<ReservationResponse> findReservations(Long conducteurId, Long trajetId);

    //terminer un trajet (apres l'heure de depart)
    TrajetDetailResponse terminer(Long conducteurId,Long trajetId);

    //annuler un trajet
    void annuler(Long conducteurId,Long trajetId);

}
