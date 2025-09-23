package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findBookingByPassenger_EmailOrderByCreatedAtDesc(String passengerEmail, Pageable pageable);

    @Query("""
            SELECT DISTINCT(b) FROM Booking b
            LEFT JOIN FETCH b.items i
            LEFT JOIN FETCH i.flight f
            JOIN FETCH b.passenger
            WHERE :id = b.id
           """
            )

        Booking searchBooking(@Param("id") Long id);

}
