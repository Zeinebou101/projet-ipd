package com.immobilier.controller;

import com.immobilier.model.Locataire;
import com.immobilier.service.LocataireService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locataires")
public class LocataireController {

    private final LocataireService locataireService;

    public LocataireController(LocataireService locataireService) {
        this.locataireService = locataireService;
    }

    @GetMapping
    public ResponseEntity<List<Locataire>> getAllLocataires() {
        return ResponseEntity.ok(locataireService.getAllLocataires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locataire> getLocataireById(@PathVariable Long id) {
        return locataireService.getLocataireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @GetMapping("/phoneNumber/{phoneNumber}")
    // public ResponseEntity<?> getLocataireByPhoneNumber(@PathVariable String phoneNumber) {
    //     return locataireService.getLocataireByPhoneNumber(phoneNumber)
    //             .map(locataire -> {
    //                 LocataireResponse response = new LocataireResponse();
    //                 response.setId(locataire.getId());
    //                 response.setName(locataire.getName());
    //                 response.setEmail(locataire.getEmail());
    //                 response.setMontantLocation(locataire.getMontantLocation());
    //                 response.setService(locataire.getService());
                    
    //                 if (locataire.getProprieteLouee() != null) {
    //                     response.setAdressePropriete(locataire.getProprieteLouee().getAdresse());
    //                 }
                    
    //                 if (locataire.getProprietaire() != null) {
    //                     response.setNomProprietaire(locataire.getProprietaire().getNom());
    //                     response.setPrenomProprietaire(locataire.getProprietaire().getPrenom());
    //                 }
                    
    //                 return ResponseEntity.ok(response);
    //             }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Locataire non trouvé")));
    // }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getLocataireCount() {
        return ResponseEntity.ok(Map.of("total", locataireService.getLocataireCount()));
    }

    @PostMapping
    public ResponseEntity<?> createLocataire(@RequestBody LocataireRequest request) {
        try {
            Locataire locataire = new Locataire();
            locataire.setName(request.getName());
            locataire.setEmail(request.getEmail());
            locataire.setPhoneNumber(request.getPhoneNumber());
            locataire.setEntryDate(request.getEntryDate());
            locataire.setMontantLocation(request.getMontantLocation());
            locataire.setService(request.getService());
            
            Locataire savedLocataire = locataireService.createLocataire(
                    locataire,
                    request.getProprietaireId(),
                    request.getProprieteLoueeId()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Locataire ajouté avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocataire(@PathVariable Long id, @RequestBody Locataire locataire) {
        try {
            Locataire updatedLocataire = locataireService.updateLocataire(id, locataire);
            return ResponseEntity.ok(Map.of("message", "Locataire modifié"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLocataire(@PathVariable Long id) {
        try {
            locataireService.deleteLocataire(id);
            return ResponseEntity.ok(Map.of("message", "Locataire supprimé"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @Data
    private static class LocataireRequest {
        private String name;
        private String email;
        private String phoneNumber;
        private LocalDate entryDate;
        private Long proprietaireId;
        private Long proprieteLoueeId;
        private Double montantLocation;
        private String service;
    }

    @Data
    private static class LocataireResponse {
        private Long id;
        private String name;
        private String email;
        private Double montantLocation;
        private String service;
        private String adressePropriete;
        private String nomProprietaire;
        private String prenomProprietaire;
    }
}