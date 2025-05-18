package com.immobilier.service;

import com.immobilier.model.Locataire;
import com.immobilier.model.Versement;
import com.immobilier.repository.LocataireRepository;
import com.immobilier.repository.VersementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VersementService {

    private final VersementRepository versementRepository;
    private final LocataireRepository locataireRepository;

    public VersementService(VersementRepository versementRepository, LocataireRepository locataireRepository) {
        this.versementRepository = versementRepository;
        this.locataireRepository = locataireRepository;
    }

    public List<Versement> getVersementsByProprietairePhoneNumber(String phoneNumber) {
        return versementRepository.findByProprietairePhoneNumber(phoneNumber);
    }

    @Transactional
    public Versement createVersement(Versement versement, Long locataireId) {
        Locataire locataire = locataireRepository.findById(locataireId)
                .orElseThrow(() -> new RuntimeException("Locataire not found with id: " + locataireId));
        
        versement.setLocataire(locataire);
        versement.setCreatedAt(LocalDateTime.now());
        versement.setUpdatedAt(LocalDateTime.now());
        
        return versementRepository.save(versement);
    }
}