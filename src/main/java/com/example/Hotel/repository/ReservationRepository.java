package com.example.Hotel.repository;

import com.example.Hotel.entities.Reservation;
import com.example.Hotel.entities.ReservationPK;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<Reservation, ReservationPK> {
    //Filtrage
    //Par Période - CheckIn
    List<Reservation> findByPK_CheckInBetween(LocalDate start, LocalDate end);
    //Par Période - CheckOut
    List<Reservation> findByPK_CheckOutBetween(LocalDate start, LocalDate end);
    
    //Par Statut
    List<Reservation> findByStatut(String statut);
    
    //Trouver une réservation par chambre et client
    Optional<Reservation> findByPK_ChambreAndPK_Client(Long chambreId, Long clientId);
    
    //Vérifier si une chambre a des réservations actives (confirmées)
    List<Reservation> findByPK_ChambreAndStatut(Long chambreId, String statut);
}
