package com.immobilier.controller;

import com.immobilier.model.Propriete;
import com.immobilier.service.ProprieteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/propriete")
public class ProprieteController {

    private final ProprieteService proprieteService;

    public ProprieteController(ProprieteService proprieteService) {
        this.proprieteService = proprieteService;
    }

    @GetMapping
    public ResponseEntity<List<Propriete>> getAllProprietes(
            @RequestParam(required = false) Long proprietaireId) {
        
        if (proprietaireId != null) {
            return ResponseEntity.ok(proprieteService.getProprietesForProprietaire(proprietaireId));
        }
        
        return ResponseEntity.ok(proprieteService.getAllProprietes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propriete> getProprieteById(@PathVariable Long id) {
        return proprieteService.getProprieteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getProprieteCount() {
        return ResponseEntity.ok(Map.of("total", proprieteService.getProprieteCount()));
    }
}