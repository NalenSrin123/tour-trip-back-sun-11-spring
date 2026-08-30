package com.etec.tourtripapi.destination.repository;

import com.etec.tourtripapi.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DestinationRepository
        extends JpaRepository<Destination, Long>,
        JpaSpecificationExecutor<Destination> {

    List<Destination> findByNameContainingIgnoreCase(String name);

    List<Destination> findByCountryIgnoreCase(String country);

    List<Destination> findByCityIgnoreCase(String city);
}
