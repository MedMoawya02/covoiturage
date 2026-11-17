package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.request.VehiculeRequest;
import com.example.covoiturage.dto.response.VehiculeResponse;
import com.example.covoiturage.entity.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(target = "conducteur_id",source = "conducteur.id")
    VehiculeResponse toResponse(Vehicule vehicule);

    @Mapping(target="id",ignore = true)
    @Mapping(target = "conducteur",ignore=true)
    Vehicule toEntity(VehiculeRequest request);

    @Mapping(target="id",ignore = true)
    @Mapping(target = "conducteur",ignore=true)
    void updateEntity(VehiculeRequest request, @MappingTarget Vehicule vehicule);
}
