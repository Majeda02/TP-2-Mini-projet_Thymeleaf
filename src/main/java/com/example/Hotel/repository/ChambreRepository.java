package com.example.Hotel.repository;

import com.example.Hotel.entities.Chambre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChambreRepository extends CrudRepository<Chambre,Long> {
    //Filtrage
    //Par Type
    List<Chambre> findByType(String type);
    //Par Disponibilté
    List<Chambre> findByDisponible(Boolean disponible);
}
