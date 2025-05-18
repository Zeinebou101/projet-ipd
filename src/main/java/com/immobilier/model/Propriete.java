package com.immobilier.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "propriete")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Propriete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proprietaireId", nullable = false)
    @JsonBackReference("proprietaire-propriete")
    private Proprietaire proprietaire;

    @Column(nullable = false)
    private String adresse;

    @Column
    private Double surface;

    @Column
    private Double valeur;

    @Column
    private String service;

    @OneToOne(mappedBy = "proprieteLouee")
    private Locataire locataire;
}