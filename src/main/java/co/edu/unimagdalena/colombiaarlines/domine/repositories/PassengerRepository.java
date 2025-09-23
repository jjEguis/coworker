package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmailIgnoreCase(String email);

    //Jpql buscar pasajero por email usando precarga fecht
    @Query("""
        SELECT p FROM Passenger p
        LEFT JOIN FETCH p.passengerProfile
        WHERE LOWER (p.email) = LOWER(:email)
        """)

    Optional<Passenger> findByEmailIgnoreCaseAndPassengerProfile(@Param("email") String email);
}
