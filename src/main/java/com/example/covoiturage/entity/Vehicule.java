package com.example.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicules")
@Getter@Setter@NoArgsConstructor
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String marque;
    @NotBlank
    @Column(nullable = false)
    private String modele;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String immatriculation;
    @NotNull
    @Min(1) @Max(8)
    @Column(nullable = false)
    private Integer capacite;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "conducteur_id",nullable = false)
    private Conducteur conducteur;

}
