package com.example.Hotel.repository;

import com.example.Hotel.entities.Chambre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ChambreRepository extends CrudRepository<Chambre,Long> {
    //Filtrage
    //Par Type
    List<Chambre> findByType(String type);
    //Par Disponibilté
    List<Chambre> findByDisponible(Boolean disponible);

    // Statistiques: nombre de chambres par type
    @Query("SELECT c.type, COUNT(c) FROM Chambre c GROUP BY c.type")
    List<Object[]> countGroupByType();

    // Statistiques: nombre de chambres occupées (disponible = false)
    @Query("SELECT COUNT(c) FROM Chambre c WHERE c.disponible = false")
    long countOccupied();
}
