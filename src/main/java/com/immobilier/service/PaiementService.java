package com.immobilier.service;

import com.immobilier.model.Locataire;
import com.immobilier.model.Paiement;
import com.immobilier.repository.LocataireRepository;
import com.immobilier.repository.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final LocataireRepository locataireRepository;

    private final Map<String, Month> moisMapping = new HashMap<>();

    public PaiementService(PaiementRepository paiementRepository, LocataireRepository locataireRepository) {
        this.paiementRepository = paiementRepository;
        this.locataireRepository = locataireRepository;
        
        // Initialize month mapping
        moisMapping.put("janvier", Month.JANUARY);
        moisMapping.put("février", Month.FEBRUARY);
        moisMapping.put("mars", Month.MARCH);
        moisMapping.put("avril", Month.APRIL);
        moisMapping.put("mai", Month.MAY);
        moisMapping.put("juin", Month.JUNE);
        moisMapping.put("juillet", Month.JULY);
        moisMapping.put("août", Month.AUGUST);
        moisMapping.put("septembre", Month.SEPTEMBER);
        moisMapping.put("octobre", Month.OCTOBER);
        moisMapping.put("novembre", Month.NOVEMBER);
        moisMapping.put("décembre", Month.DECEMBER);
    }

    public List<Paiement> getPaiementsByLocataireId(Long locataireId) {
        return paiementRepository.findByLocataireId(locataireId);
    }

    public List<Paiement> getAllArrieres() {
        return paiementRepository.findAllArrieres();
    }

    public List<Paiement> getArrieresByLocataireId(Long locataireId) {
        return paiementRepository.findArrieresByLocataireId(locataireId);
    }

    public Long countArrieres() {
        return paiementRepository.countArrieres();
    }

    public List<Object[]> getRapportMensuel(int year) {
        return paiementRepository.findRapportMensuel(year);
    }

    @Transactional
    public Paiement createPaiement(Paiement paiement, Long locataireId) {
        Locataire locataire = locataireRepository.findById(locataireId)
                .orElseThrow(() -> new RuntimeException("Locataire not found with id: " + locataireId));
        
        paiement.setLocataire(locataire);
        
        // Calculate the due date
        LocalDate dateEcheance = calculateDateEcheance(paiement.getMois());
        paiement.setDateEcheance(dateEcheance);
        
        return paiementRepository.save(paiement);
    }

    @Transactional
    public Paiement updatePaiement(Long id, Paiement paiement) {
        return paiementRepository.findById(id)
                .map(existingPaiement -> {
                    existingPaiement.setMois(paiement.getMois());
                    existingPaiement.setMontant(paiement.getMontant());
                    existingPaiement.setEtat(paiement.getEtat());
                    
                    if (paiement.getLocataire() != null) {
                        Locataire locataire = locataireRepository.findById(paiement.getLocataire().getId())
                                .orElseThrow(() -> new RuntimeException("Locataire not found"));
                        existingPaiement.setLocataire(locataire);
                    }
                    
                    return paiementRepository.save(existingPaiement);
                })
                .orElseThrow(() -> new RuntimeException("Paiement not found with id: " + id));
    }

    private LocalDate calculateDateEcheance(String mois) {
        Month month = moisMapping.get(mois.toLowerCase());
        if (month == null) {
            throw new RuntimeException("Invalid month: " + mois);
        }
        
        int year = LocalDate.now().getYear();
        
        // For December, the due date is January 1st of the next year
        if (month == Month.DECEMBER) {
            return LocalDate.of(year + 1, Month.JANUARY, 1);
        } else {
            // For other months, the due date is the 1st of the next month
            return LocalDate.of(year, month.plus(1), 1);
        }
    }
}