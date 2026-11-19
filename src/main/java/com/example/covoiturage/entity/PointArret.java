package com.example.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="points_arret",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trajet_id", "ville"}))
@Getter@Setter@NoArgsConstructor
public class PointArret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String ville;
    @NotNull@Min(0)
    @Column(nullable = false)
    private Integer ordre;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime heurePassage;
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixDepuisDepart;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "trajet_id",nullable = false)
    private Trajet trajet;
}
