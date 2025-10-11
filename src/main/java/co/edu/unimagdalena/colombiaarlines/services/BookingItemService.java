package co.edu.unimagdalena.colombiaarlines.services;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;

import java.math.BigDecimal;
import java.util.List;

public interface BookingItemService {
    BookingItemResponse addItem(Long bookingId, BookingItemCreateRequest req);
    List<BookingItemResponse> findByBookingIdSegmentOrder(Long id);
    BigDecimal getTotalPrice(Long id);
    Long seatsSold(Long id, Cabin cabin);
    BookingItemResponse updateBookingItem(BookingItemUpdateRequest req);
    List<BookingItemResponse> list();
    void delete(Long id);
}
