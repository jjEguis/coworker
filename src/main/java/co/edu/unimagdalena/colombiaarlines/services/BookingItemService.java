package co.edu.unimagdalena.colombiaarlines.services;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;

import java.util.List;

public interface BookingItemService {
    BookingItemResponse create(BookingItemCreateRequest req);
    List<BookingItemResponse> findByBookingIdSegmentOrder(Long id);
    void getTotalPrice(Long id);
    void seatsSold(Long id);
    BookingItemResponse updateBookingItem(BookingItemUpdateRequest req);
    List<BookingItemResponse> list();
    void delete(Long id);
}
