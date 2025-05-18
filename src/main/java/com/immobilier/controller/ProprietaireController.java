package com.immobilier.controller;

import com.immobilier.model.Proprietaire;
import com.immobilier.model.Propriete;
import com.immobilier.service.ProprietaireService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proprietaires")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    public ProprietaireController(ProprietaireService proprietaireService) {
        this.proprietaireService = proprietaireService;
    }

    @GetMapping
    public ResponseEntity<List<Proprietaire>> getAllProprietaires() {
        return ResponseEntity.ok(proprietaireService.getAllProprietaires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietaire> getProprietaireById(@PathVariable Long id) {
        return proprietaireService.getProprietaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getProprietaireCount() {
        return ResponseEntity.ok(Map.of("total", proprietaireService.getProprietaireCount()));
    }

    @PostMapping
    public ResponseEntity<?> createProprietaire(@RequestBody ProprietaireRequest request) {
        try {
            Proprietaire proprietaire = new Proprietaire();
            System.out.println(request.getNom());
            proprietaire.setNom(request.getNom());
            proprietaire.setPrenom(request.getPrenom());
            proprietaire.setAdresse(request.getAdresse());
            proprietaire.setPhoneNumber(request.getPhoneNumber());
            proprietaire.setEmail(request.getEmail());
            
            List<Propriete> proprietes = new ArrayList<>();
            for (ProprieteDTO prop : request.getPropriete()) {
                Propriete propriete = new Propriete();
                propriete.setAdresse(prop.getAdresse());
                propriete.setSurface(prop.getSurface());
                propriete.setValeur(prop.getValeur());
                propriete.setService(prop.getService());
                proprietes.add(propriete);
            }
            
            Proprietaire savedProprietaire = proprietaireService.createProprietaire(proprietaire, proprietes);
            return ResponseEntity.status(HttpStatus.CREATED).body("Propriétaire et propriétés ajoutés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProprietaire(@PathVariable Long id, @RequestBody ProprietaireRequest request) {
        try {
            Proprietaire proprietaire = new Proprietaire();
            proprietaire.setNom(request.getNom());
            proprietaire.setPrenom(request.getPrenom());
            proprietaire.setAdresse(request.getAdresse());
            proprietaire.setPhoneNumber(request.getPhoneNumber());
            proprietaire.setEmail(request.getEmail());
            
            List<Propriete> proprietes = new ArrayList<>();
            for (ProprieteDTO prop : request.getPropriete()) {
                Propriete propriete = new Propriete();
                propriete.setAdresse(prop.getAdresse());
                propriete.setSurface(prop.getSurface());
                propriete.setValeur(prop.getValeur());
                propriete.setService(prop.getService());
                proprietes.add(propriete);
            }
            
            Proprietaire updatedProprietaire = proprietaireService.updateProprietaire(id, proprietaire, proprietes);
            return ResponseEntity.ok("Propriétaire et propriétés modifiés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProprietaire(@PathVariable Long id) {
        try {
            proprietaireService.deleteProprietaire(id);
            return ResponseEntity.ok("Propriétaire et propriétés supprimés");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Data
    private static class ProprietaireRequest {
        private String nom;
        private String prenom;
        private String adresse;
        private String phoneNumber;
        private String email;
        private List<ProprieteDTO> propriete;
    }

    @Data
    private static class ProprieteDTO {
        private String adresse;
        private Double surface;
        private Double valeur;
        private String service;
    }
}