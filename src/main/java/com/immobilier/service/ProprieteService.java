package com.immobilier.service;

import com.immobilier.model.Propriete;
import com.immobilier.repository.ProprieteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprieteService {

    private final ProprieteRepository proprieteRepository;

    public ProprieteService(ProprieteRepository proprieteRepository) {
        this.proprieteRepository = proprieteRepository;
    }

    public List<Propriete> getAllProprietes() {
        return proprieteRepository.findAll();
    }

    public List<Propriete> getProprietesForProprietaire(Long proprietaireId) {
        return proprieteRepository.findByProprietaireId(proprietaireId);
    }

    public Optional<Propriete> getProprieteById(Long id) {
        return proprieteRepository.findById(id);
    }

    public long getProprieteCount() {
        return proprieteRepository.count();
    }
}