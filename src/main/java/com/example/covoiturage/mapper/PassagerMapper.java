package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.request.PassagerRequest;
import com.example.covoiturage.dto.request.RegisterPassagerRequest;
import com.example.covoiturage.dto.response.PassagerResponse;
import com.example.covoiturage.entity.Passager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassagerMapper {
    PassagerResponse toResponse(Passager passager);

    @Mapping(target = "id",ignore = true)
    @Mapping(target="role",constant = "PASSAGER")
    @Mapping(target = "active",constant = "true")
    @Mapping(target = "reservations",ignore = true)
    Passager toEntity(RegisterPassagerRequest request);

}
