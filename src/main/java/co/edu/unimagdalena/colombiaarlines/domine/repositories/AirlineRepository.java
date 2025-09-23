package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AirlineRepository extends CrudRepository<Airline, Long> {
    Optional<Airline>findAirlinesByCode(String code);

}
