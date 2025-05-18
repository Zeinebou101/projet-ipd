package com.immobilier.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locataires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column
    private LocalDate entryDate;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    @JsonBackReference("proprietaire-locataire")
    private Proprietaire proprietaire;

    @OneToOne
    @JoinColumn(name = "proprieteLoueeId")
    private Propriete proprieteLouee;

    @Column
    private Double montantLocation;

    @Column
    private String service;

    @OneToMany(mappedBy = "locataire", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "locataire", cascade = CascadeType.ALL)
    private List<Versement> versements = new ArrayList<>();
}