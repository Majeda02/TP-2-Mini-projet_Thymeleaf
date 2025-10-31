package com.example.Hotel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nom;
    private String passeport;

    @OneToMany(mappedBy="client", cascade= CascadeType.ALL)
    private List<Reservation> reservations;

    public Client() {
    }

    public Client(Long id, String nom, String passeport) {
        this.id = id;
        this.nom = nom;
        this.passeport = passeport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPasseport() {
        return passeport;
    }

    public void setPasseport(String passeport) {
        this.passeport = passeport;
    }
}
