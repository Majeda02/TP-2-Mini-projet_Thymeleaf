package com.example.Hotel.service;

import com.example.Hotel.entities.Chambre;
import com.example.Hotel.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ReservationService reservationService;

    public double getTauxOccupationSimple() {
        long total = chambreService.countChambres();
        if (total == 0) return 0.0;
        // Compter les chambres non disponibles
        long occupees = chambreService.getChambresByDisponibilite(false).size();
        return (occupees / (double) total) * 100.0;
    }

    public BigDecimal getRevPARSimple() {
        long totalChambres = chambreService.countChambres();
        if (totalChambres == 0) return BigDecimal.ZERO;
        List<Reservation> reservations = reservationService.getAllReservations();
        BigDecimal revenuTotal = reservations.stream()
                .map(Reservation::getTotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return revenuTotal.divide(BigDecimal.valueOf(totalChambres), 2, RoundingMode.HALF_UP);
    }


    public Map<String, Long> getRepartitionChambresParType() {
        Map<String, Long> out = new HashMap<>();
        for (Chambre c : chambreService.getAllChambres()) {
            String key = c.getType();
            out.put(key, out.getOrDefault(key, 0L) + 1);
        }
        return out;
    }

    public Map<String, Long> getReservationsParStatut() {
        Map<String, Long> out = new HashMap<>();
        for (Reservation r : reservationService.getAllReservations()) {
            String key = r.getStatut();
            out.put(key, out.getOrDefault(key, 0L) + 1);
        }
        return out;
    }
}


