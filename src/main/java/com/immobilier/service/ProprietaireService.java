package com.immobilier.service;

import com.immobilier.model.Proprietaire;
import com.immobilier.model.Propriete;
import com.immobilier.repository.ProprietaireRepository;
import com.immobilier.repository.ProprieteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietaireService {

    private final ProprietaireRepository proprietaireRepository;
    private final ProprieteRepository proprieteRepository;

    public ProprietaireService(ProprietaireRepository proprietaireRepository, ProprieteRepository proprieteRepository) {
        this.proprietaireRepository = proprietaireRepository;
        this.proprieteRepository = proprieteRepository;
    }

    public List<Proprietaire> getAllProprietaires() {
        return proprietaireRepository.findAll();
    }

    public Optional<Proprietaire> getProprietaireById(Long id) {
        return proprietaireRepository.findById(id);
    }

    public long getProprietaireCount() {
        return proprietaireRepository.count();
    }

    @Transactional
    public Proprietaire createProprietaire(Proprietaire proprietaire, List<Propriete> proprietes) {
        // Check if email already exists
        if (proprietaireRepository.existsByEmail(proprietaire.getEmail())) {
            throw new RuntimeException("Email already exists, please choose another one.");
        }

        // Check if phone number already exists
        if (proprietaireRepository.existsByPhoneNumber(proprietaire.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists, please choose another one.");
        }

        proprietaire.setPropertiesCount(proprietes.size());
        Proprietaire savedProprietaire = proprietaireRepository.save(proprietaire);

        // Add properties
        proprietes.forEach(propriete -> {
            propriete.setProprietaire(savedProprietaire);
            proprieteRepository.save(propriete);
        });

        return savedProprietaire;
    }

    @Transactional
    public Proprietaire updateProprietaire(Long id, Proprietaire proprietaire, List<Propriete> proprietes) {
        return proprietaireRepository.findById(id)
                .map(existingProprietaire -> {
                    existingProprietaire.setNom(proprietaire.getNom());
                    existingProprietaire.setPrenom(proprietaire.getPrenom());
                    existingProprietaire.setAdresse(proprietaire.getAdresse());
                    existingProprietaire.setPhoneNumber(proprietaire.getPhoneNumber());
                    existingProprietaire.setEmail(proprietaire.getEmail());
                    
                    // Delete existing properties
                    proprieteRepository.deleteAll(existingProprietaire.getProprietes());
                    
                    // Add new properties
                    proprietes.forEach(propriete -> {
                        propriete.setProprietaire(existingProprietaire);
                        proprieteRepository.save(propriete);
                    });
                    
                    existingProprietaire.setPropertiesCount(proprietes.size());
                    return proprietaireRepository.save(existingProprietaire);
                })
                .orElseThrow(() -> new RuntimeException("Proprietaire not found with id: " + id));
    }

    @Transactional
    public void deleteProprietaire(Long id) {
        proprietaireRepository.deleteById(id);
    }
}