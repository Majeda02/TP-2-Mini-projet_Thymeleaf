package com.example.Hotel.repository;

import com.example.Hotel.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
