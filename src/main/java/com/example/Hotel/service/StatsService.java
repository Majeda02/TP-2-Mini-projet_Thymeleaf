package com.example.Hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import com.example.Hotel.repository.ChambreRepository;
import com.example.Hotel.repository.ReservationRepository;


@Service
public class StatsService {

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientService clientService;

    public double getTauxOccupationSimple() {
        long total = chambreRepository.count();
        if (total == 0) return 0.0;
        long occupees = chambreRepository.countOccupied();
        return (occupees / (double) total) * 100.0;
    }

    public BigDecimal getRevPARSimple() {
        long totalChambres = chambreRepository.count();
        if (totalChambres == 0) return BigDecimal.ZERO;
        BigDecimal revenuTotal = reservationRepository.sumTotalRevenue();
        return revenuTotal.divide(BigDecimal.valueOf(totalChambres), 2, RoundingMode.HALF_UP);
    }


    public Map<String, Long> getRepartitionChambresParType() {
        Map<String, Long> out = new HashMap<>();
        for (Object[] row : chambreRepository.countGroupByType()) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            out.put(type, count);
        }
        return out;
    }

    public Map<String, Long> getReservationsParStatut() {
        Map<String, Long> out = new HashMap<>();
        for (Object[] row : reservationRepository.countGroupByStatut()) {
            String statut = (String) row[0];
            Long count = (Long) row[1];
            out.put(statut, count);
        }
        return out;
    }

    public long getTotalChambres() {
        return chambreRepository.count();
    }

    public long getTotalClients() {
        return clientService.countClients();
    }

    public long getTotalReservations() {
        return reservationRepository.count();
    }
}


