package com.example.Hotel.controller;

import com.example.Hotel.entities.Chambre;
import com.example.Hotel.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chambres")
@CrossOrigin(origins = "http://localhost:3000")
public class ChambreRestController {

    @Autowired
    private ChambreService chambreService;

    @GetMapping
    public List<Chambre> getAllChambres(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean disponible) {
        if (type != null && !type.isEmpty()) {
            return chambreService.getChambresByType(type);
        } else if (disponible != null) {
            return chambreService.getChambresByDisponibilite(disponible);
        }
        return chambreService.getAllChambres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Long id) {
        return chambreService.getChambreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Chambre createChambre(@RequestBody Chambre chambre) {
        if (chambre.getDisponible() == null) {
            chambre.setDisponible(true);
        }
        return chambreService.saveChambre(chambre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChambre(@PathVariable Long id, @RequestBody Chambre chambre) {
        try {
            chambre.setId(id);
            if (chambre.getDisponible() == null) {
                chambre.setDisponible(true);
            }
            Chambre updatedChambre = chambreService.saveChambre(chambre);
            return ResponseEntity.ok(updatedChambre);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\", \"type\": \"warning\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Erreur lors de la mise à jour de la chambre\", \"type\": \"danger\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        chambreService.deleteChambre(id);
        return ResponseEntity.ok().build();
    }
}

