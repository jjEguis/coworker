package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookingItemRepository extends JpaRepository<BookingItem,Long> {

    List<BookingItem> findBookingItemByBookingIdOrderBySegmentOrder(Long bookingId);

    @Query("""
            SELECT SUM(COALESCE(bi.price)) FROM BookingItem bi
            LEFT JOIN bi.booking b
            WHERE :id = b.id
           """)
    BigDecimal getTotalPrice(@Param("id") Long id);


    @Query("""
            SELECT COUNT(bi) FROM BookingItem bi
            JOIN bi.flight f
            WHERE :id = f.id
            AND :cabin = bi.cabin
            """)
    Long seatsSold(@Param("id") Long id,
                   @Param("cabin") Cabin cabin);

}