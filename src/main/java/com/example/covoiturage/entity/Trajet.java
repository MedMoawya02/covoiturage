package com.example.covoiturage.entity;

import com.example.covoiturage.enums.StatutTrajet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trajets")
@Getter@Setter@NoArgsConstructor
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String villeDepart;
    @NotBlank
    @Column(nullable = false)
    private String villeArrivee;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateDepart;
    @NotNull@Min(1)
    @Column(nullable = false)
    private Integer placesProposees;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTrajet statut;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "conducteur_id",nullable = false)
    private Conducteur conducteur;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "vehicule_id",nullable = false)
    private Vehicule vehicule;

    @OneToMany(mappedBy = "trajet",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @OrderBy("ordre ASC")
    private List<PointArret> pointsArret=new ArrayList<>();
    @OneToMany(mappedBy = "trajet",fetch = FetchType.LAZY)
    private List<Reservation> reservations=new ArrayList<>();

    public void addPointArret(PointArret pa) {
        pa.setTrajet(this);
        this.pointsArret.add(pa);
    }


}
