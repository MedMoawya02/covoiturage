package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.PointArretRequest;
import com.example.covoiturage.dto.request.TrajetRequest;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.*;
import com.example.covoiturage.enums.StatutReservation;
import com.example.covoiturage.enums.StatutTrajet;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.exception.ForbiddenException;
import com.example.covoiturage.exception.ResourceNotFoundException;
import com.example.covoiturage.mapper.PointArretMapper;
import com.example.covoiturage.mapper.ReservationMapper;
import com.example.covoiturage.mapper.TrajetMapper;
import com.example.covoiturage.mapper.VehiculeMapper;
import com.example.covoiturage.repository.*;
import com.example.covoiturage.service.TrajetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {
    private final ConducteurRepository conducteurRepository;
    private final TrajetRepository trajetRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationRepository reservationRepository;
    private final TrajetMapper trajetMapper;
    private final ReservationMapper reservationMapper;
    private final PointArretMapper pointArretMapper;
    @Override
    public TrajetDetailResponse publier(Long conducteurId, TrajetRequest request) {
        Conducteur conducteur=conducteurRepository.findById(conducteurId).orElseThrow(()->new ResourceNotFoundException("Conducteur introuvable : "+conducteurId));
        //vehicule doit appartenir à un conducteur
        Vehicule vehicule=vehiculeRepository.findById(request.vehiculeId()).orElseThrow(()->new ResourceNotFoundException("Véhicule introuvable : "+request.vehiculeId()));
        if (!vehicule.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Le véhicule n'appartient pas à ce conducteur");
        }
        //au moin 2 points d'arret
        List<PointArretRequest> points=request.pointsArret();
        if(points==null||points.size()<2){
            throw new BusinessRuleException("2 points d'arret au moins");
        }
        //trier par ordre croissant
        points=points.stream()
                .sorted(Comparator.comparing(PointArretRequest::ordre))
                .collect(Collectors.toList());
        //ordre commence par 0
        for (int i=0;i<points.size();i++){
            if(points.get(i).ordre()!=i){throw new BusinessRuleException(                     "Les ordres des points d'arrêt doivent être 0, 1, 2, ... sans saut");}
        }
        //villes unique pour le trajet
        Set<String> villes=new HashSet<>();
        for(PointArretRequest p:points){
            if(!villes.add(p.ville().trim().toLowerCase())){
                throw new BusinessRuleException("Ville en double sur le parcours : "+p.ville());
            }
        }
        //heures strictement croissantes
        for(int i=1;i<points.size();i++){
            if(!points.get(i).heurePassage().isAfter(points.get(i-1).heurePassage())){

                throw new BusinessRuleException(
                        "Les heures de passage doivent être strictement croissantes");
            }
        }
        //le depart dans le futur
        LocalDateTime heureDepart=points.get(0).heurePassage();
        if(!heureDepart.isAfter(LocalDateTime.now())){
            throw new BusinessRuleException("Le départ doit être dans le futur");
        }
        //prix=0 au depart puis strictement croissant

            if(points.get(0).prixDepuisDepart().compareTo(BigDecimal.ZERO)!=0){
                throw new BusinessRuleException("Le prix au premier point doit être 0");}
            for (int i=1;i<points.size();i++){
                if(points.get(i).prixDepuisDepart().compareTo(points.get(i - 1).prixDepuisDepart())<=0){
                    throw new BusinessRuleException(
                            "Les prix depuis le départ doivent être strictement croissants");
                }
            }

        //place proposéées entre 1 et capacité du vehicule
        if(request.placesProposees()<1||request.placesProposees()>vehicule.getCapacite()){
            throw new BusinessRuleException("Les places proposées doivent etre entre 1 et "+vehicule.getCapacite());
        }
        //creation du trajet
        Trajet trajet=new Trajet();
        trajet.setConducteur(conducteur);
        trajet.setVehicule(vehicule);
        trajet.setDateDepart(heureDepart);
        trajet.setPlacesProposees(request.placesProposees());
        trajet.setStatut(StatutTrajet.PUBLIE);
        trajet.setVilleDepart(points.get(0).ville());
        trajet.setVilleArrivee(points.get(points.size()-1).ville());
        //creation des points d'arret
        for (PointArretRequest p:points){
            PointArret pa=new PointArret();
            pa.setVille(p.ville());
            pa.setOrdre(p.ordre());
            pa.setHeurePassage(p.heurePassage());
            pa.setPrixDepuisDepart(p.prixDepuisDepart());
            trajet.addPointArret(pa);
        }
        Trajet saved=trajetRepository.save(trajet);

        return trajetMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TrajetDetailResponse findById(Long id) {
        Trajet trajet=trajetRepository.findByIdWithDetails(id).orElseThrow(()->new ResourceNotFoundException("Vehicule introuvable : "+id));
        return trajetMapper.toDetailResponse(trajet);
    }

    @Override

    public List<TrajetResponse> findByConducteur(Long conducteurId) {
        if(!conducteurRepository.existsById(conducteurId)){
            throw new ResourceNotFoundException("Conducteur introuvable : "+conducteurId);
        }
        return trajetRepository.findByConducteurId(conducteurId)
                .stream()
                .map(trajetMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> findReservations(Long conducteurId, Long trajetId) {
        Trajet trajet=trajetRepository.findById(trajetId).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable : "+trajetId));
        if(!trajet.getConducteur().getId().equals(conducteurId)){
            throw new BusinessRuleException("Ce trajet n'appartient pas à ce conducteur");
        }

        return reservationRepository.findByTrajetId(trajetId).stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrajetDetailResponse terminer(Long conducteurId, Long trajetId) {
        Trajet trajet=trajetRepository.findById(trajetId).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable : "+trajetId));
        if(!trajet.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Ce trajet n'appartient pas à ce conducteur : "+conducteurId);
        }
        if(trajet.getStatut()==StatutTrajet.TERMINE||trajet.getStatut()==StatutTrajet.ANNULE){
            throw new BusinessRuleException("Ne peut pas terminer ce trajet.");
        }
        if (LocalDateTime.now().isBefore(trajet.getDateDepart())) {
            throw new BusinessRuleException("Impossible de terminer un trajet avant son départ");
        }
        trajet.setStatut(StatutTrajet.TERMINE);
        Trajet saved=trajetRepository.save(trajet);
        return trajetMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional
    public void annuler(Long conducteurId, Long trajetId) {
        Trajet trajet=trajetRepository.findById(trajetId).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable : "+trajetId));
        if (!trajet.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Ce trajet n'appartient pas à ce conducteur");
        }
        if (trajet.getStatut()==StatutTrajet.TERMINE||trajet.getStatut()==StatutTrajet.ANNULE){
            throw new BusinessRuleException("Ne peut pas annuler un trajet : "+ trajet.getStatut());
        }
        //annuler tous les reservations
        List<Reservation> reservations=reservationRepository.findByTrajetId(trajetId);
        for(Reservation r:reservations){
            if(r.getStatut()==StatutReservation.CONFIRMEE){
                r.setStatut(StatutReservation.ANNULEE);
            }
        }
        reservationRepository.saveAll(reservations);
        trajet.setStatut(StatutTrajet.ANNULE);
        trajetRepository.save(trajet);
    }
}
