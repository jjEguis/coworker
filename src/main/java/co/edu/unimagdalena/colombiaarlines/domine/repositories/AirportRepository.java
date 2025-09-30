package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);

}
