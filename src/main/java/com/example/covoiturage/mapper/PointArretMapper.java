package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.request.PointArretRequest;
import com.example.covoiturage.dto.response.PointArretResponse;
import com.example.covoiturage.entity.PointArret;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointArretMapper {
    PointArretResponse toResponse(PointArret pointArret);
    List<PointArret> toResponseList(List<PointArret>points);

    @Mapping(target = "id",ignore = true)
    @Mapping(target="trajet",ignore = true)
    PointArret toEntity(PointArretRequest request);
}
