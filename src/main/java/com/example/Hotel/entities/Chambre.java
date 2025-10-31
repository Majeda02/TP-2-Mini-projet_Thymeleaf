package com.example.Hotel.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="chambres")
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String numero;
    public String type;
    public BigDecimal prixNuit;
    public Boolean disponible;

    @OneToMany(mappedBy="chambre", cascade=CascadeType.ALL)
    private List<Reservation> reservations;

    public Chambre() {
    }

    public Chambre(Long id, String numero, String type, BigDecimal prixNuit, Boolean disponible) {
        this.id = id;
        this.numero = numero;
        this.type = type;
        this.prixNuit = prixNuit;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrixNuit() {
        return prixNuit;
    }

    public void setPrixNuit(BigDecimal prixNuit) {
        this.prixNuit = prixNuit;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
