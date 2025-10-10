package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingItemRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BookingItemServiceImpl implements BookingItemService {

    private final BookingItemRepository repo;
    private final FlightRepository flightRepo;

    @Override
    public BookingItemResponse create(BookingItemCreateRequest req) {
        // Buscar entidades relacionadas
        Booking booking = repo.findById(req.bookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found")).getBooking();
        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        // Mapear DTO -> Entidad
        BookingItem bookingItem = BookingItemMapper.toEntity(req, booking, flight);

        // Guardar
        repo.save(bookingItem);

        // Devolver respuesta
        return BookingItemMapper.toResponse(bookingItem);
    }

    @Override
    public BookingItemResponse updateBookingItem(BookingItemUpdateRequest req) {
        // Buscar BookingItem existente
        BookingItem bookingItem = repo.findById(req.flightId()) // OJO si usas otro id, cambia aquí
                .orElseThrow(() -> new RuntimeException("BookingItem not found"));

        // Buscar vuelo relacionado si se actualiza
        Flight flight = req.flightId() != null
                ? flightRepo.findById(req.flightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"))
                : bookingItem.getFlight();

        // Actualizar la entidad
        BookingItemMapper.updateEntity(bookingItem, req, flight);

        repo.save(bookingItem);

        return BookingItemMapper.toResponse(bookingItem);
    }

    @Override
    public List<BookingItemResponse> findByBookingIdSegmentOrder(Long bookingId) {
        List<BookingItem> items = repo.findBookingItemByBookingIdOrderBySegmentOrder(bookingId);

        if (items.isEmpty()) {
            throw new RuntimeException("No BookingItems found for bookingId " + bookingId);
        }

        return items.stream()
                .map(BookingItemMapper::toResponse)
                .toList();
    }



    @Override
    public void getTotalPrice(Long id) {
        BigDecimal total = repo.getTotalPrice(id);
        System.out.println("Total price for booking " + id + ": " + total);
    }

    @Override
    public void seatsSold(Long id) {
        // Ejemplo: podrías consultar para cada cabina si quisieras
        for (Cabin cabin : Cabin.values()) {
            Long count = repo.seatsSold(id, cabin);
            System.out.println("Seats sold for flight " + id + " in " + cabin + ": " + count);
        }
    }

    @Override
    public List<BookingItemResponse> list() {
        return repo.findAll()
                .stream()
                .map(BookingItemMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("BookingItem not found");
        }
        repo.deleteById(id);
    }
}
