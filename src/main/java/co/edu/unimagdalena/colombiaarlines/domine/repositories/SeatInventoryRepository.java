package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    Optional<SeatInventory> findSeatInventoriesByFlight_IdAndCabin(Long flightId, Cabin cabin);

    @Query("""
           SELECT CASE WHEN COUNT(si.cabin) > :min THEN TRUE ELSE FALSE END
           FROM SeatInventory si
           JOIN Flight f ON si.flight.id = f.id
           WHERE :flightId = f.id
           AND :cabin = si.cabin
           """
    )
    
    boolean availableSeats(@Param("flightId")Long flightId,
                           @Param("cabin")Cabin cabin,
                           @Param("min") int min);

    Optional<SeatInventory> findByFlightIdAndCabin(Long flight_id, Cabin cabin);
}
