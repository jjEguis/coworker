package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingUpdateRequest;

import java.util.List;

public interface BookingService {

    BookingResponse create(BookingCreateRequest req);

    BookingResponse get(Long id);
    
    List<BookingResponse> list();

    BookingResponse update(Long id, BookingUpdateRequest req);

    void delete(Long id);
}