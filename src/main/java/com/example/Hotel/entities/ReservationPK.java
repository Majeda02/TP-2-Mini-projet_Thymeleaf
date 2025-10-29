package com.example.Hotel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class ReservationPK implements Serializable {

    @Column(name = "chambre_id")
    private Long chambre;
    @Column(name = "client_id")
    private Long client ;

    private LocalDate checkIn;
    private LocalDate checkOut;


    public ReservationPK() {
    }

    public ReservationPK(Long chambre, Long client, LocalDate checkIn, LocalDate checkOut) {
        this.chambre = chambre;
        this.client = client;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getChambre() {
        return chambre;
    }

    public void setChambre(Long chambre) {
        this.chambre = chambre;
    }

    public Long getClient() {
        return client;
    }

    public void setClient(Long client) {
        this.client = client;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationPK that = (ReservationPK) o;
        return java.util.Objects.equals(chambre, that.chambre) &&
                java.util.Objects.equals(client, that.client) &&
                java.util.Objects.equals(checkIn, that.checkIn) &&
                java.util.Objects.equals(checkOut, that.checkOut);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(chambre, client, checkIn, checkOut);
    }
}
