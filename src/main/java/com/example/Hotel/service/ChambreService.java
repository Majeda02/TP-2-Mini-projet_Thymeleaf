package com.example.Hotel.service;

import com.example.Hotel.entities.Chambre;
import com.example.Hotel.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ChambreService {

    @Autowired
    private ChambreRepository chambreRepository;
    
    @Autowired
    @Lazy
    private ReservationService reservationService;

    public List<Chambre> getAllChambres() {
        return (List<Chambre>) chambreRepository.findAll();
    }

    public Optional<Chambre> getChambreById(Long id) {
        return chambreRepository.findById(id);
    }

    public List<Chambre> getChambresByType(String type) {
        return chambreRepository.findByType(type);
    }

    public List<Chambre> getChambresByDisponibilite(Boolean disponible) {
        return chambreRepository.findByDisponible(disponible);
    }

    public Chambre saveChambre(Chambre chambre) {
        // Si la chambre existe déjà, vérifier si on essaie de modifier sa disponibilité
        if (chambre.getId() != null) {
            // Récupérer l'état actuel de la chambre
            Optional<Chambre> existingChambreOpt = chambreRepository.findById(chambre.getId());
            if (existingChambreOpt.isPresent()) {
                Chambre existingChambre = existingChambreOpt.get();
                
                // Si la disponibilité change ET qu'il y a des réservations actives
                if (!existingChambre.getDisponible().equals(chambre.getDisponible()) && 
                    reservationService.hasActiveReservations(chambre.getId())) {
                    throw new IllegalStateException(" Cette chambre ne peut pas être modifiée car elle a des réservations confirmées.");
                }
            }
        }
        return chambreRepository.save(chambre);
    }

    public void deleteChambre(Long id) {
        chambreRepository.deleteById(id);
    }

    public long countChambres() {
        return chambreRepository.count();
    }
}




