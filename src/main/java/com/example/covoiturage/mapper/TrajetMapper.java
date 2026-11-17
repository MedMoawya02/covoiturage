package com.example.covoiturage.mapper;

import com.example.covoiturage.dto.response.TrajetDetailResponse;
import com.example.covoiturage.dto.response.TrajetResponse;
import com.example.covoiturage.entity.Trajet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {
        ConducteurMapper.class,
        VehiculeMapper.class,
        PointArretMapper.class

})
public interface TrajetMapper {
    @Mapping(target="conducteurNomComplet",expression = "java(trajet.getConducteur().getPrenom()+\"\"+trajet.getConducteur().getNom())")
    @Mapping(target = "vehiculeInfo", expression = "java(trajet.getVehicule().getMarque() + \" \" + trajet.getVehicule().getModele() + \" (\" + trajet.getVehicule().getImmatriculation() + \")\")")
    @Mapping(target = "placesRestantes", ignore = true)
    TrajetResponse toResponse(Trajet trajet);
    @Mapping(target = "placesRestantes", ignore = true)
    TrajetDetailResponse toDetailResponse(Trajet trajet);

}
