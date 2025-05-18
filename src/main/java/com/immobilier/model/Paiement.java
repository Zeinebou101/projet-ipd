package com.immobilier.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "locataire_id", nullable = false)
    private Locataire locataire;

    @Column(nullable = false)
    private String mois;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String etat;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Column
    private LocalDate dateEcheance;

    @Column
    private Double caution;

    @Column
    private String modePaiement;
}