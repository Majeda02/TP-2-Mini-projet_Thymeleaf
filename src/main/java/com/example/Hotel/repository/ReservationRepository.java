package com.example.Hotel.repository;

import com.example.Hotel.entities.Reservation;
import com.example.Hotel.entities.ReservationPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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

    // Statistiques: somme des totaux (revenu total)
    @Query("SELECT COALESCE(SUM(r.total), 0) FROM Reservation r")
    BigDecimal sumTotalRevenue();

    // Statistiques: nombre de réservations par statut
    @Query("SELECT r.statut, COUNT(r) FROM Reservation r GROUP BY r.statut")
    List<Object[]> countGroupByStatut();

    // Contrainte: chevauchement de période pour la même chambre (exclut les annulées)
    @Query("SELECT r FROM Reservation r WHERE r.PK.chambre = :chambreId AND r.statut <> 'Annulée' AND r.PK.checkIn < :end AND r.PK.checkOut > :start")
    List<Reservation> findOverlaps(@Param("chambreId") Long chambreId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
