package com.example.Hotel.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="reservations")
public class Reservation {
    @EmbeddedId
    private ReservationPK PK;

    private String statut;
    private BigDecimal total;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chambre_id", insertable = false, updatable = false)
    private Chambre chambre;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client;

    public Reservation() {
    }

    public Reservation(ReservationPK PK, String statut, BigDecimal total, Chambre chambre, Client client) {
        this.PK = PK;
        this.statut = statut;
        this.total = total;
        this.chambre = chambre;
        this.client = client;
    }

    public ReservationPK getPK() {
        return PK;
    }

    public void setPK(ReservationPK PK) {
        this.PK = PK;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}