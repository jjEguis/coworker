package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingItemRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BookingItemServiceImpl implements BookingItemService {

    private final BookingItemRepository repo;
    private final BookingRepository bookingRepo;
    private final FlightRepository flightRepo;

    @Override
    public BookingItemResponse addItem(Long bookingId , BookingItemCreateRequest req) {
        // 1️⃣ Buscar el Booking
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        // 2️⃣ Buscar el Flight asociado
        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + req.flightId()));

        // 3️⃣ Mapear DTO → Entidad
        BookingItem bookingItem = BookingItemMapper.toEntity(req, booking, flight);

        // 4️⃣ Guardar el item
        repo.save(bookingItem);

        // 5️⃣ Asociar el item al booking (si la relación es bidireccional)
        booking.getItems().add(bookingItem);
        bookingRepo.save(booking);

        // 6️⃣ Devolver la respuesta
        return BookingItemMapper.toResponse(bookingItem);
    }

    @Override
    public BookingItemResponse updateBookingItem(BookingItemUpdateRequest req) {
        // Buscar BookingItem existente
        BookingItem bookingItem = repo.findById(req.flightId()) // OJO si usas otro id, cambia aquí
                .orElseThrow(() -> new NotFoundException("BookingItem not found"));

        // Buscar vuelo relacionado si se actualiza
        Flight flight = flightRepo.findById(req.flightId())
        .orElseThrow(() -> new NotFoundException("Flight not found"));

        // Actualizar la entidad
        BookingItemMapper.updateEntity(bookingItem, req, flight);

        repo.save(bookingItem);

        return BookingItemMapper.toResponse(bookingItem);
    }

    @Override @Transactional(readOnly = true)
    public List<BookingItemResponse> findByBookingIdSegmentOrder(Long bookingId) {
        List<BookingItem> items = repo.findBookingItemByBookingIdOrderBySegmentOrder(bookingId);

        if (items.isEmpty()) {
            throw new NotFoundException("No BookingItems found for bookingId " + bookingId);
        }

        return items.stream()
                .map(BookingItemMapper::toResponse)
                .toList();
    }



    @Override @Transactional(readOnly = true)
    public BigDecimal getTotalPrice(Long id) {
        return repo.getTotalPrice(id);
    }

    @Override
    public Long seatsSold(Long id,Cabin cabin) {
        flightRepo.findById(id).orElseThrow(() -> new NotFoundException("Flight not found"));
        return repo.seatsSold(id, cabin);
    }

    @Override @Transactional(readOnly = true)
    public List<BookingItemResponse> list() {
        return repo.findAll()
                .stream()
                .map(BookingItemMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("BookingItem not found");
        }
        repo.deleteById(id);
    }
}
