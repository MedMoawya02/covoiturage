package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.response.ReservationResponse;
import com.example.covoiturage.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "trajetId", source = "trajet.id")
    @Mapping(target = "villeDepart", source = "trajet.villeDepart")
    @Mapping(target = "villeArrivee", source = "trajet.villeArrive")
    @Mapping(target = "pointMonteeVille", source = "pointMontee.ville")
    @Mapping(target = "pointDescenteVille", source = "pointDescente.ville")
    ReservationResponse toResponse(Reservation reservation);

    List<ReservationResponse> toResponseList(List<Reservation> reservations);
}