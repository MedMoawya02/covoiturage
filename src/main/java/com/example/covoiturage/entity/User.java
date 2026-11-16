package com.example.covoiturage.entity;

import com.example.covoiturage.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter@Setter@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Le nom est requis")
    @Column(nullable = false)
    private String nom;
    @NotBlank(message = "Le prenom est requis")
    @Column(nullable = false)
    private String prenom;
    @Email
    @NotBlank(message = "L'email est requis")
    @Column(nullable = false,unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String telephone;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isActive = true;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
