package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findByCode(String code);
}
