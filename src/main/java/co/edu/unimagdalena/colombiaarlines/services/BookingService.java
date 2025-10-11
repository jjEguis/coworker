package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookingService {

    BookingResponse create(BookingCreateRequest req);
    Page<BookingResponse> findByPassenger_Email(String email, Pageable pageable);
    BookingResponse update(Long id, BookingUpdateRequest req);
    BookingResponse searchBooking(Long id);
    void delete(Long id);
}