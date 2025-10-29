package com.example.Hotel.service;

import com.example.Hotel.entities.Chambre;
import com.example.Hotel.entities.Client;
import com.example.Hotel.entities.Reservation;
import com.example.Hotel.entities.ReservationPK;
import com.example.Hotel.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    @Lazy
    private ChambreService chambreService;

    @Autowired
    private ClientService clientService;

    public List<Reservation> getAllReservations() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationByChambreAndClient(Long chambreId, Long clientId) {
        return reservationRepository.findByPK_ChambreAndPK_Client(chambreId, clientId);
    }

    public Optional<Reservation> getReservationByPK(ReservationPK pk) {
        return reservationRepository.findById(pk);
    }

    public List<Reservation> getReservationsByCheckIn(LocalDate start, LocalDate end) {
        return reservationRepository.findByPK_CheckInBetween(start, end);
    }

    public List<Reservation> getReservationsByCheckOut(LocalDate start, LocalDate end) {
        return reservationRepository.findByPK_CheckOutBetween(start, end);
    }

    public List<Reservation> getReservationsByStatut(String statut) {
        return reservationRepository.findByStatut(statut);
    }

    public Reservation saveReservation(Reservation reservation) {
        // Mettre à jour la disponibilité de la chambre
        if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
            Optional<Chambre> chambreOpt = chambreService.getChambreById(reservation.getChambre().getId());
            if (chambreOpt.isPresent()) {
                Chambre chambre = chambreOpt.get();
                // Ne changer la disponibilité que si le statut est "Confirmée"
                if ("Confirmée".equals(reservation.getStatut())) {
                    chambre.setDisponible(false);
                }
                chambreService.saveChambre(chambre);
            }
        }
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(ReservationPK pk, Reservation updatedReservation) {
        Optional<Reservation> existingOpt = reservationRepository.findById(pk);
        if (existingOpt.isPresent()) {
            Reservation existing = existingOpt.get();
            Long chambreId = existing.getChambre() != null ? existing.getChambre().getId() : null;
            
            // Sauvegarder la réservation mise à jour
            updatedReservation.setPK(pk);
            updatedReservation.setChambre(existing.getChambre());
            updatedReservation.setClient(existing.getClient());
            Reservation savedReservation = reservationRepository.save(updatedReservation);
            
            // Gérer la disponibilité de la chambre selon le changement de statut
            if (chambreId != null) {
                boolean wasConfirmed = "Confirmée".equals(existing.getStatut());
                boolean isNowConfirmed = "Confirmée".equals(updatedReservation.getStatut());
                
                if (wasConfirmed && !isNowConfirmed) {
                    // Réservation déconfirmée, libérer la chambre si plus de réservations confirmées
                    if (!hasActiveReservations(chambreId)) {
                        Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
                        if (chambreOpt.isPresent()) {
                            Chambre chambre = chambreOpt.get();
                            chambre.setDisponible(true);
                            chambreService.saveChambre(chambre);
                        }
                    }
                } else if (!wasConfirmed && isNowConfirmed) {
                    // Réservation confirmée, bloquer la chambre
                    Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
                    if (chambreOpt.isPresent()) {
                        Chambre chambre = chambreOpt.get();
                        chambre.setDisponible(false);
                        chambreService.saveChambre(chambre);
                    }
                }
            }
            
            return savedReservation;
        }
        throw new RuntimeException("Réservation non trouvée");
    }

    public void deleteReservation(ReservationPK pk) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(pk);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Long chambreId = reservation.getChambre() != null ? reservation.getChambre().getId() : null;
            
            // Supprimer la réservation d'abord
            reservationRepository.deleteById(pk);
            
            // Libérer la chambre seulement si c'était une réservation confirmée 
            // et qu'il n'y a plus d'autres réservations confirmées pour cette chambre
            if (chambreId != null && "Confirmée".equals(reservation.getStatut())) {
                // Vérifier s'il reste des réservations confirmées pour cette chambre
                if (!hasActiveReservations(chambreId)) {
                    Optional<Chambre> chambreOpt = chambreService.getChambreById(chambreId);
                    if (chambreOpt.isPresent()) {
                        Chambre chambre = chambreOpt.get();
                        chambre.setDisponible(true);
                        chambreService.saveChambre(chambre);
                    }
                }
            }
        }
    }

    public long countReservations() {
        return reservationRepository.count();
    }
    
    /**
     * Vérifie si une chambre a des réservations confirmées (actives)
     * @param chambreId ID de la chambre
     * @return true si la chambre a des réservations confirmées, false sinon
     */
    public boolean hasActiveReservations(Long chambreId) {
        List<Reservation> activeReservations = reservationRepository.findByPK_ChambreAndStatut(chambreId, "Confirmée");
        return !activeReservations.isEmpty();
    }
}

