package com.immobilier.repository;

import com.immobilier.model.Locataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocataireRepository extends JpaRepository<Locataire, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Locataire> findByPhoneNumber(String phoneNumber);
}