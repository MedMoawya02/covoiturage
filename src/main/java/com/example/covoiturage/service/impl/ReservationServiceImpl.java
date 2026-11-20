package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.ReservationRequest;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.entity.Passager;
import com.example.covoiturage.entity.PointArret;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.entity.Trajet;
import com.example.covoiturage.enums.StatutReservation;
import com.example.covoiturage.enums.StatutTrajet;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.exception.ResourceNotFoundException;
import com.example.covoiturage.mapper.ReservationMapper;
import com.example.covoiturage.repository.PassagerRepository;
import com.example.covoiturage.repository.PointArretRepository;
import com.example.covoiturage.repository.ReservationRepository;
import com.example.covoiturage.repository.TrajetRepository;
import com.example.covoiturage.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final PassagerRepository passagerRepository;
    private final TrajetRepository trajetRepository;
    private final ReservationRepository reservationRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;
    @Override
    public ReservationResponse reserver(Long passagerId, ReservationRequest request) {

        Passager passager=passagerRepository.findById(passagerId).orElseThrow(()->new ResourceNotFoundException("Passager introuvable : "+passagerId));

        Trajet trajet=trajetRepository.findById(request.trajetId()).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable : "+request.trajetId()));

        if(trajet.getStatut()== StatutTrajet.COMPLET){
            throw new BusinessRuleException("Ce trajet deja complet "+trajet.getStatut());
        }
        if (trajet.getStatut()==StatutTrajet.ANNULE||trajet.getStatut()==StatutTrajet.TERMINE){
            throw new BusinessRuleException("Impossible de réserver sur un trajet " + trajet.getStatut());
        }

        //verification des points(montee et descente)
        PointArret pointMontee=findPointInTrajet(trajet, request.pointMonteeId());
        PointArret pointDescente=findPointInTrajet(trajet, request.pointDescenteId());
        //pointMonte<pointMontee(ordre)
        if(pointMontee.getOrdre()>pointDescente.getOrdre()){
            throw new BusinessRuleException("Le point de montée doit être avant le point de descente");
        }

        //encore des places
        int placesDemandees=request.nombrePlaces();
        if (placesDemandees < 1) {
            throw new BusinessRuleException("Le nombre de places doit être au moins 1");
        }
        int placesReste=calculerPlacesRestantes(trajet,pointMontee,pointDescente);
        if (placesDemandees > placesReste) {
            throw new BusinessRuleException(
                    "Places insuffisantes sur ce segment. Restantes : " + placesReste);
        }

        //calculdu totale
        BigDecimal prixSegment=pointMontee.getPrixDepuisDepart().subtract(pointMontee.getPrixDepuisDepart());
        BigDecimal montantTotal=prixSegment.multiply(BigDecimal.valueOf(placesDemandees)).setScale(2, RoundingMode.HALF_UP);

        //creation de rsesrvation
        Reservation reservation=new Reservation();
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);
        reservation.setNombrePlaces(placesDemandees);
        reservation.setMontantTotal(montantTotal);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved=reservationRepository.save(reservation);

        // places restantes sur le segment
        int placesRestantes=calculerPlacesRestantes(trajet,pointMontee,pointDescente);
        if(placesRestantes==0){
            trajet.setStatut(StatutTrajet.COMPLET);
            trajetRepository.save(trajet);
        }

        return reservationMapper.toResponse(saved);
    }

    @Override
    public ReservationResponse findById(Long reservationId) {
        return null;
    }

    @Override
    public List<ReservationResponse> findByPassager(Long passagerId) {
        return List.of();
    }

    @Override
    public void annuler(Long passagerId, Long reservationId) {

    }

    //Méthodes utilitaires
    private PointArret findPointInTrajet(Trajet trajet,Long pointId){
        return trajet.getPointsArret().stream()
                .filter(pointArret -> pointArret.getId().equals(pointId))
                .findFirst().orElseThrow(()->new BusinessRuleException("Point d'arret "+pointId+" introuvable sur ce trajet"))
                ;
    }

    private int calculerPlacesRestantes(Trajet trajet,PointArret monte,PointArret descente){
        List<Reservation> reservations=reservationRepository.findByTrajetId(trajet.getId());

        int placesUtilise=0;

        for (Reservation r:reservations){
            //filtrer les reservations confirmé
            if(r.getStatut()!= StatutReservation.CONFIRMEE)continue;
            // Chevauchement : segments [m1, d1] et [m2, d2] se chevauchent si
            // m1 < d2 ET m2 < d1
            int m1=r.getPointMontee().getOrdre();
            int d1=r.getPointDescente().getOrdre();
            int m2=monte.getOrdre();
            int d2=monte.getOrdre();
            if(m1<d2 && m2<d1){
                placesUtilise+=r.getNombrePlaces();
            }
        }
        return trajet.getPlacesProposees()-placesUtilise;
    }
}
