package com.example.covoiturage.service.impl;

import com.example.covoiturage.dto.request.PassagerRequest;
import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.entity.Passager;
import com.example.covoiturage.entity.Reservation;
import com.example.covoiturage.exception.BusinessRuleException;
import com.example.covoiturage.exception.ResourceNotFoundException;
import com.example.covoiturage.mapper.PassagerMapper;
import com.example.covoiturage.mapper.ReservationMapper;
import com.example.covoiturage.repository.PassagerRepository;
import com.example.covoiturage.repository.ReservationRepository;
import com.example.covoiturage.repository.UserRepository;
import com.example.covoiturage.service.PassagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassagerServiceImpl implements PassagerService {
    private final PassagerRepository passagerRepository;
    private final UserRepository userRepository;
    private final PassagerMapper passagerMapper;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    @Override
    public Page<PassagerResponse> findAll(Pageable pageable) {
        return passagerRepository.findAll(pageable).map(passagerMapper::toResponse);
    }

    @Override
    public PassagerResponse findById(Long id) {
        Passager passager=passagerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        return passagerMapper.toResponse(passager);
    }

    @Override
    public Page<PassagerResponse> search(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return findAll(pageable);
        }
        return passagerRepository.searchByNomOrEmail(q.trim(), pageable)
                .map(passagerMapper::toResponse);
    }

    @Override
    public PassagerResponse update(Long id, PassagerRequest request) {
        Passager passager=passagerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        if(!passager.getEmail().equals(request.email())&&passagerRepository.existsByEmail(request.email())){
            throw new ResourceNotFoundException("Email deja existe : "+request.email());
        }
        passager.setNom(request.nom());
        passager.setPrenom(request.prenom());
        passager.setEmail(request.email());
        passager.setTelephone(request.telephone());
        Passager saved=passagerRepository.save(passager);
        return passagerMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Passager passager=passagerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Id introuvable : "+id));
        if(!passager.getReservations().isEmpty()){
            throw new BusinessRuleException("Ne peut pas supprimer un passager ayant reservations");
        }
        passagerRepository.delete(passager);

    }

    @Override
    public List<ReservationResponse> findReservations(Long passagerId) {
        if (!passagerRepository.existsById(passagerId)){
            throw new ResourceNotFoundException("Passager introuvable");
        }
        List<Reservation> reservations=reservationRepository.findByPassagerId(passagerId);
        return reservationMapper.toResponseList(reservations);
    }
}
