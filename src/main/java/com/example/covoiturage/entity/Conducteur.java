package com.example.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conducteurs")
@Getter@Setter@NoArgsConstructor
public class Conducteur extends User {
    @NotBlank
    @Column(nullable = false,unique = true,name = "numero_permis")
    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Vehicule> vehicules=new ArrayList<>();

    @OneToMany(mappedBy = "conducteur")
    private List<Trajet> trajets=new ArrayList<>();

}
