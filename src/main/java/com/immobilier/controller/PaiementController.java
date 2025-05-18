package com.immobilier.controller;

import com.immobilier.model.Locataire;
import com.immobilier.model.Paiement;
import com.immobilier.service.LocataireService;
import com.immobilier.service.PaiementService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaiementController {

    private final PaiementService paiementService;
    private final LocataireService locataireService;

    public PaiementController(PaiementService paiementService, LocataireService locataireService) {
        this.paiementService = paiementService;
        this.locataireService = locataireService;
    }

    @GetMapping("/paiements/{locataire_id}")
    public ResponseEntity<List<Paiement>> getPaiementsByLocataireId(@PathVariable("locataire_id") Long locataireId) {
        return ResponseEntity.ok(paiementService.getPaiementsByLocataireId(locataireId));
    }

    @PostMapping("/paiements")
    public ResponseEntity<?> createPaiement(@RequestBody PaiementRequest request) {
        try {
            Paiement paiement = new Paiement();
            paiement.setMois(request.getMois());
            paiement.setMontant(request.getMontant());
            paiement.setEtat(request.getEtat());
            paiement.setDatePaiement(LocalDate.now());
            paiement.setCaution(request.getCaution());
            paiement.setModePaiement(request.getModePaiement());
            
            Paiement savedPaiement = paiementService.createPaiement(paiement, request.getLocataireId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Paiement enregistré avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/paiements/{id}")
    public ResponseEntity<?> updatePaiement(@PathVariable Long id, @RequestBody PaiementUpdateRequest request) {
        try {
            Paiement paiement = new Paiement();
            
            Locataire locataire = locataireService.getLocataireById(request.getLocataireId())
                    .orElseThrow(() -> new RuntimeException("Locataire not found"));
            
            paiement.setLocataire(locataire);
            paiement.setMois(request.getMois());
            paiement.setMontant(request.getMontant());
            paiement.setEtat(request.getEtat());
            
            Paiement updatedPaiement = paiementService.updatePaiement(id, paiement);
            return ResponseEntity.ok(updatedPaiement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/arrieres")
    public ResponseEntity<List<Paiement>> getAllArrieres() {
        return ResponseEntity.ok(paiementService.getAllArrieres());
    }

    @GetMapping("/arrieres/{locataireId}")
    public ResponseEntity<List<Paiement>> getArrieresByLocataireId(@PathVariable Long locataireId) {
        return ResponseEntity.ok(paiementService.getArrieresByLocataireId(locataireId));
    }

    @GetMapping("/arrieres/count")
    public ResponseEntity<Map<String, Long>> countArrieres() {
        return ResponseEntity.ok(Map.of("total", paiementService.countArrieres()));
    }

    @GetMapping("/rapport/mensuel")
    public ResponseEntity<List<Object[]>> getRapportMensuel(@RequestParam int year) {
        return ResponseEntity.ok(paiementService.getRapportMensuel(year));
    }

    @Data
    private static class PaiementRequest {
        private Long locataireId;
        private String mois;
        private Double montant;
        private String etat;
        private Double caution;
        private String modePaiement;
    }

    @Data
    private static class PaiementUpdateRequest {
        private Long locataireId;
        private String mois;
        private Double montant;
        private String etat;
    }
}