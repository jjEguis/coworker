package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.BookingUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingItemMapper;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository; // para obtener el pasajero por id o email
    private final FlightRepository flightRepository;

    @Override
    public BookingResponse create(BookingCreateRequest req) {
        // Buscar pasajero
        var passenger = passengerRepository.findById(req.passengerId())
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        // Crear entidad
        Booking booking = Booking.builder()
                .createdAt(OffsetDateTime.now())
                .passenger(passenger)
                .items(new ArrayList<>()) // Se inicializa vacía
                .build();

        bookingRepository.save(booking);
        return BookingMapper.toResponse(booking);
    }

    @Override
    public Page<BookingResponse> findByPassenger_Email(String email, Pageable pageable) {
        var bookings = bookingRepository.findBookingByPassenger_EmailOrderByCreatedAtDesc(email, pageable);
        return bookings.map(BookingMapper::toResponse);
    }

    @Override
    public BookingResponse update(Long id, BookingUpdateRequest req) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (req.items() != null && !req.items().isEmpty()) {
            // Mapeamos cada BookingItemUpdateRequest a BookingItem
            var updatedItems = req.items().stream()
                    .map(itemReq -> BookingItemMapper.toEntity(itemReq,booking,flight))
                    .toList();

            booking.setItems(updatedItems);
        }

        bookingRepository.save(booking);
        return BookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse searchBooking(Long id) {
        var booking = bookingRepository.searchBooking(id);
        if (booking == null) throw new RuntimeException("Booking not found");

        return BookingMapper.toResponse(booking);
    }

    @Override
    public void delete(Long id) {
        var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        bookingRepository.delete(booking);
    }
}