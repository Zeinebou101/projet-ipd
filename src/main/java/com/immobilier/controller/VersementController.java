package com.immobilier.controller;

import com.immobilier.model.Versement;
import com.immobilier.service.VersementService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/versements")
public class VersementController {

    private final VersementService versementService;

    public VersementController(VersementService versementService) {
        this.versementService = versementService;
    }

    @GetMapping
    public ResponseEntity<List<Versement>> getVersements(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(versementService.getVersementsByProprietairePhoneNumber(phoneNumber));
    }

    @PostMapping
    public ResponseEntity<?> createVersement(@RequestBody VersementRequest request) {
        try {
            Versement versement = new Versement();
            versement.setNomProprietaire(request.getNomProprietaire());
            versement.setMontant(request.getMontant());
            versement.setDateVersement(request.getDateVersement());
            
            Versement savedVersement = versementService.createVersement(versement, request.getLocataireId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVersement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Data
    private static class VersementRequest {
        private Long locataireId;
        private String nomProprietaire;
        private Double montant;
        private LocalDate dateVersement;
    }
}