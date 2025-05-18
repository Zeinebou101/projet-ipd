package com.immobilier.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proprietaires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proprietaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column
    private String adresse;

    @Column(name="phoneNumber",nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)    
    private String email;

    @Column(name = "PropertiesCount")
    private Integer propertiesCount = 0;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("proprietaire-propriete")
    private List<Propriete> proprietes = new ArrayList<>();

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    @JsonManagedReference("proprietaire-locataire")
    private List<Locataire> locataires = new ArrayList<>();
}