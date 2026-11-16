package com.example.covoiturage.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passagers")
@Getter@Setter@NoArgsConstructor
public class Passager extends User{
    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservations=new ArrayList<>();
}
