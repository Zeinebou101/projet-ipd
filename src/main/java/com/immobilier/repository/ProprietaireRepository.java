package com.immobilier.repository;

import com.immobilier.model.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Proprietaire> findByPhoneNumber(String phoneNumber);
}