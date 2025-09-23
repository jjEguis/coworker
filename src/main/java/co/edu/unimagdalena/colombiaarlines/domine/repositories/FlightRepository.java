package co.edu.unimagdalena.colombiaarlines.domine.repositories;


import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByAirlineName(String name, Pageable pageable);

    Page<Flight> findFlightByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
            String originCode, String destinationCode,
            OffsetDateTime from, OffsetDateTime to, Pageable pageable);

     @Query("""
        SELECT DISTINCT f FROM Flight f
        LEFT JOIN FETCH f.airline
        LEFT JOIN FETCH f.origin
        LEFT JOIN FETCH f.destination
        JOIN FETCH f.tags
        WHERE (:origin IS NULL OR f.origin = :origin)
              AND (:destination IS NULL OR f.destination = :destination)
              AND f.departureTime BETWEEN :from AND :to
        """
     )

    List<Flight> searchFlight(@Param("origin")Airport origin,
                              @Param("destination")Airport destination,
                              @Param("from")OffsetDateTime from,
                              @Param("to")OffsetDateTime to);


     @Query(value = """
             SELECT f.*
            FROM flights f
            JOIN flight_tags ft ON f.id = ft.flight_id
            JOIN tags t ON t.id = ft.tag_id
            WHERE t.name IN (:tags)
            GROUP BY f.id
            HAVING COUNT(DISTINCT t.name) = :required
            """, nativeQuery = true)

     List<Flight> findFlightsWithAllTags(
             @Param("tags") Collection<String> tags,
             @Param("required") int required);
}
