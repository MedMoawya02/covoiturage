package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.request.ConducteurRequest;
import com.example.covoiturage.dto.request.RegisterConducteurRequest;
import com.example.covoiturage.dto.response.ConducteurResponse;
import com.example.covoiturage.entity.Conducteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConducteurMapper {
    ConducteurResponse toResponse(Conducteur conducteur);

    @Mapping(target="id",ignore=true)
    @Mapping(target = "role",constant = "CONDUCTEUR")
    @Mapping(target = "active",constant = "true")
    @Mapping(target="vehicules",ignore = true)
    @Mapping(target="trajets",ignore=true)
    Conducteur toEntity(RegisterConducteurRequest request);

}
