package com.immobilier.service;

import com.immobilier.model.Locataire;
import com.immobilier.model.Proprietaire;
import com.immobilier.model.Propriete;
import com.immobilier.repository.LocataireRepository;
import com.immobilier.repository.ProprietaireRepository;
import com.immobilier.repository.ProprieteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocataireService {

    private final LocataireRepository locataireRepository;
    private final ProprietaireRepository proprietaireRepository;
    private final ProprieteRepository proprieteRepository;

    public LocataireService(LocataireRepository locataireRepository, 
                           ProprietaireRepository proprietaireRepository,
                           ProprieteRepository proprieteRepository) {
        this.locataireRepository = locataireRepository;
        this.proprietaireRepository = proprietaireRepository;
        this.proprieteRepository = proprieteRepository;
    }

    public List<Locataire> getAllLocataires() {
        return locataireRepository.findAll();
    }

    public Optional<Locataire> getLocataireById(Long id) {
        return locataireRepository.findById(id);
    }

    public Optional<Locataire> getLocataireByPhoneNumber(String phoneNumber) {
        return locataireRepository.findByPhoneNumber(phoneNumber);
    }

    public long getLocataireCount() {
        return locataireRepository.count();
    }

    @Transactional
    public Locataire createLocataire(Locataire locataire, Long proprietaireId, Long proprieteId) {
        // Check if email already exists
        if (locataireRepository.existsByEmail(locataire.getEmail())) {
            throw new RuntimeException("Email already exists, please choose another one.");
        }

        // Check if phone number already exists
        if (locataireRepository.existsByPhoneNumber(locataire.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists, please choose another one.");
        }

        // Get proprietaire
        Proprietaire proprietaire = proprietaireRepository.findById(proprietaireId)
                .orElseThrow(() -> new RuntimeException("Proprietaire not found with id: " + proprietaireId));
        locataire.setProprietaire(proprietaire);

        // Get propriete
        Propriete propriete = proprieteRepository.findById(proprieteId)
                .orElseThrow(() -> new RuntimeException("Propriete not found with id: " + proprieteId));
        locataire.setProprieteLouee(propriete);

        return locataireRepository.save(locataire);
    }

    @Transactional
    public Locataire updateLocataire(Long id, Locataire locataire) {
        return locataireRepository.findById(id)
                .map(existingLocataire -> {
                    existingLocataire.setName(locataire.getName());
                    existingLocataire.setEmail(locataire.getEmail());
                    existingLocataire.setPhoneNumber(locataire.getPhoneNumber());
                    existingLocataire.setEntryDate(locataire.getEntryDate());
                    existingLocataire.setMontantLocation(locataire.getMontantLocation());
                    existingLocataire.setService(locataire.getService());
                    
                    if (locataire.getProprietaire() != null) {
                        Proprietaire proprietaire = proprietaireRepository.findById(locataire.getProprietaire().getId())
                                .orElseThrow(() -> new RuntimeException("Proprietaire not found"));
                        existingLocataire.setProprietaire(proprietaire);
                    }
                    
                    if (locataire.getProprieteLouee() != null) {
                        Propriete propriete = proprieteRepository.findById(locataire.getProprieteLouee().getId())
                                .orElseThrow(() -> new RuntimeException("Propriete not found"));
                        existingLocataire.setProprieteLouee(propriete);
                    }
                    
                    return locataireRepository.save(existingLocataire);
                })
                .orElseThrow(() -> new RuntimeException("Locataire not found with id: " + id));
    }

    @Transactional
    public void deleteLocataire(Long id) {
        locataireRepository.deleteById(id);
    }
}