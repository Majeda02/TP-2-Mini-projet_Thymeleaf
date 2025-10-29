package com.example.Hotel.controller;

import com.example.Hotel.entities.*;
import com.example.Hotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String listReservations(Model model,
                                   @RequestParam(required = false) String checkInStart,
                                   @RequestParam(required = false) String checkInEnd,
                                   @RequestParam(required = false) String checkOutStart,
                                   @RequestParam(required = false) String checkOutEnd,
                                   @RequestParam(required = false) String statut) {
        List<Reservation> reservations;

        if (statut != null && !statut.isEmpty()) {
            reservations = reservationService.getReservationsByStatut(statut);
        } else if (checkInStart != null && !checkInStart.isEmpty() && checkInEnd != null && !checkInEnd.isEmpty()) {
            LocalDate start = LocalDate.parse(checkInStart);
            LocalDate end = LocalDate.parse(checkInEnd);
            reservations = reservationService.getReservationsByCheckIn(start, end);
        } else if (checkOutStart != null && !checkOutStart.isEmpty() && checkOutEnd != null && !checkOutEnd.isEmpty()) {
            LocalDate start = LocalDate.parse(checkOutStart);
            LocalDate end = LocalDate.parse(checkOutEnd);
            reservations = reservationService.getReservationsByCheckOut(start, end);
        } else {
            reservations = reservationService.getAllReservations();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("checkInStart", checkInStart);
        model.addAttribute("checkInEnd", checkInEnd);
        model.addAttribute("checkOutStart", checkOutStart);
        model.addAttribute("checkOutEnd", checkOutEnd);
        model.addAttribute("statut", statut);
        return "reservations/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("chambres", chambreService.getAllChambres());
        model.addAttribute("clients", clientService.getAllClients());
        return "reservations/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long chambreId,
                        @RequestParam Long clientId,
                        @RequestParam String checkIn,
                        @RequestParam String checkOut,
                        @RequestParam String statut,
                        @RequestParam BigDecimal total,
                        RedirectAttributes redirectAttributes) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);

            ReservationPK pk = new ReservationPK(chambreId, clientId, checkInDate, checkOutDate);
            
            Chambre chambre = chambreService.getChambreById(chambreId)
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
            
            Client client = clientService.getClientById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));

            Reservation reservation = new Reservation(pk, statut, total, chambre, client);
            reservationService.saveReservation(reservation);
            
            redirectAttributes.addFlashAttribute("message", "Réservation créée avec succès!");
            return "redirect:/reservations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création: " + e.getMessage());
            return "redirect:/reservations/create";
        }
    }

    @GetMapping("/edit/{chambreId}/{clientId}/{checkIn}/{checkOut}")
    public String editForm(@PathVariable Long chambreId,
                           @PathVariable Long clientId,
                           @PathVariable String checkIn,
                           @PathVariable String checkOut,
                           Model model) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            
            ReservationPK pk = new ReservationPK(chambreId, clientId, checkInDate, checkOutDate);
            Optional<Reservation> reservationOpt = reservationService.getReservationByPK(pk);
            
            if (reservationOpt.isPresent()) {
                model.addAttribute("reservation", reservationOpt.get());
            } else {
                // Fallback: essayer de trouver par chambre et client seulement
                reservationService.getReservationByChambreAndClient(chambreId, clientId)
                    .ifPresent(reservation -> {
                        // S'assurer que le PK contient les dates pour préremplir le formulaire
                        if (reservation.getPK() == null || reservation.getPK().getCheckIn() == null || reservation.getPK().getCheckOut() == null) {
                            reservation.setPK(new ReservationPK(chambreId, clientId, checkInDate, checkOutDate));
                        }
                        model.addAttribute("reservation", reservation);
                    });
            }
            
            // Conserver les anciennes dates pour l'action du formulaire si nécessaire
            model.addAttribute("oldCheckIn", checkIn);
            model.addAttribute("oldCheckOut", checkOut);

            model.addAttribute("chambres", chambreService.getAllChambres());
            model.addAttribute("clients", clientService.getAllClients());
            return "reservations/form";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de la réservation: " + e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping("/edit/{chambreId}/{clientId}/{checkIn}/{checkOut}")
    public String update(@PathVariable Long chambreId,
                        @PathVariable Long clientId,
                        @PathVariable String checkIn,
                        @PathVariable String checkOut,
                        @RequestParam String newCheckIn,
                        @RequestParam String newCheckOut,
                        @RequestParam String statut,
                        @RequestParam BigDecimal total,
                        RedirectAttributes redirectAttributes) {
        try {
            LocalDate oldCheckInDate = LocalDate.parse(checkIn);
            LocalDate oldCheckOutDate = LocalDate.parse(checkOut);
            LocalDate newCheckInDate = LocalDate.parse(newCheckIn);
            LocalDate newCheckOutDate = LocalDate.parse(newCheckOut);

            ReservationPK oldPk = new ReservationPK(chambreId, clientId, oldCheckInDate, oldCheckOutDate);
            ReservationPK newPk = new ReservationPK(chambreId, clientId, newCheckInDate, newCheckOutDate);
            
            Chambre chambre = chambreService.getChambreById(chambreId)
                    .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
            
            Client client = clientService.getClientById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));

            Reservation reservation = new Reservation(newPk, statut, total, chambre, client);
            reservationService.updateReservation(oldPk, reservation);
            
            redirectAttributes.addFlashAttribute("message", "Réservation mise à jour avec succès!");
            return "redirect:/reservations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour: " + e.getMessage());
            return "redirect:/reservations/edit/" + chambreId + "/" + clientId + "/" + checkIn + "/" + checkOut;
        }
    }

    @GetMapping("/delete/{chambreId}/{clientId}/{checkIn}/{checkOut}")
    public String delete(@PathVariable Long chambreId,
                        @PathVariable Long clientId,
                        @PathVariable String checkIn,
                        @PathVariable String checkOut,
                        RedirectAttributes redirectAttributes) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            
            ReservationPK pk = new ReservationPK(chambreId, clientId, checkInDate, checkOutDate);
            reservationService.deleteReservation(pk);
            redirectAttributes.addFlashAttribute("message", "Réservation supprimée avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/reservations";
    }
}
