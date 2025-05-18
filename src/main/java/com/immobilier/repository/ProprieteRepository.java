package com.immobilier.repository;

import com.immobilier.model.Propriete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProprieteRepository extends JpaRepository<Propriete, Long> {
    List<Propriete> findByProprietaireId(Long proprietaireId);
    Long countByProprietaireId(Long proprietaireId);
}