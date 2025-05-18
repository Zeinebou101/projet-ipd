package com.immobilier.repository;

import com.immobilier.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByLocataireId(Long locataireId);
    
    @Query("SELECT p FROM Paiement p WHERE p.dateEcheance < CURRENT_DATE AND p.etat != 'payé'")
    List<Paiement> findAllArrieres();
    
    @Query("SELECT p FROM Paiement p WHERE p.dateEcheance < CURRENT_DATE AND p.etat != 'payé' AND p.locataire.id = ?1")
    List<Paiement> findArrieresByLocataireId(Long locataireId);
    
    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.dateEcheance < CURRENT_DATE AND p.etat != 'payé'")
    Long countArrieres();
    
    @Query("SELECT p.mois, SUM(p.montant) FROM Paiement p WHERE YEAR(p.datePaiement) = ?1 GROUP BY p.mois")
    List<Object[]> findRapportMensuel(int year);
}