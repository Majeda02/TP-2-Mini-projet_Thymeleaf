package com.example.Hotel.controller;

import com.example.Hotel.entities.*;
import com.example.Hotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationRestController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        try {
            LocalDate checkInDate = LocalDate.parse(request.checkIn);
            LocalDate checkOutDate = LocalDate.parse(request.checkOut);

            ReservationPK pk = new ReservationPK(request.chambreId, request.clientId, checkInDate, checkOutDate);
            
            Chambre chambre = chambreService.getChambreById(request.chambreId)
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
            
            Client client = clientService.getClientById(request.clientId)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));

            Reservation reservation = new Reservation(pk, request.statut, request.total, chambre, client);
            Reservation saved = reservationService.saveReservation(reservation);
            
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{chambreId}/{clientId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long chambreId, @PathVariable Long clientId) {
        try {
            ReservationPK pk = new ReservationPK();
            pk.setChambre(chambreId);
            pk.setClient(clientId);
            reservationService.deleteReservation(pk);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class ReservationRequest {
        public Long chambreId;
        public Long clientId;
        public String checkIn;
        public String checkOut;
        public String statut;
        public BigDecimal total;
    }
}

