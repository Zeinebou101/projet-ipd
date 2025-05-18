package com.immobilier.repository;

import com.immobilier.model.Versement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersementRepository extends JpaRepository<Versement, Long> {
    @Query("SELECT v FROM Versement v JOIN v.locataire l JOIN l.proprietaire p WHERE p.phoneNumber = ?1")
    List<Versement> findByProprietairePhoneNumber(String phoneNumber);
}