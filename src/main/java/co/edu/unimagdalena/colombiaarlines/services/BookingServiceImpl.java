package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryResponse;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingItemMapper;
import co.edu.unimagdalena.colombiaarlines.services.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final PassengerRepository passengerRepo;
    private final FlightRepository flightRepo;
    private final SeatInventoryService seatInventoryService;

    private final BookingMapper bookingMapper;
    private final BookingItemMapper bookingItemMapper;

    @Override
    public BookingResponse create(BookingCreateRequest req) {
        Passenger passenger = passengerRepo.findById(req.passengerId())
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(req.passengerId())));

        Set<BookingItem> items = req.items().stream()
                .map(this::createBookingItem)
                .collect(Collectors.toSet()); // Usa set como en los DTOs

        Booking booking = bookingMapper.toEntity(req);
        if (booking.getCreatedAt() == null) {
            booking.setCreatedAt(OffsetDateTime.now());
        }

        items.forEach(item -> item.setBooking(booking));

        return bookingMapper.toResponse(bookingRepo.save(booking));
    }

    /**
     * Lógica auxiliar para crear, validar asiento y mapear un solo BookingItem.
     */
    private BookingItem createBookingItem(BookingItemCreateRequest req) {
        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight %d not found for booking item".formatted(req.flightId())));

        SeatInventoryResponse inventory = seatInventoryService.findByFlightAndCabin(req.flightId(), req.cabin().name());

        // Asumimos que se necesita al menos 1 asiento
        if (inventory.availableSeats() < 1) {
            throw new IllegalStateException("No available seats found for Flight %d in Cabin %s".formatted(req.flightId(), req.cabin()));
        }

        BookingItem item = bookingItemMapper.toEntity(req);
        item.setFlight(flight);
        return item;
    }

    @Override
    public BookingResponse update(Long id, BookingUpdateRequest req) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));

        Set<BookingItem> updatedItems = req.items().stream()
                .map(this::createBookingItem)
                .collect(Collectors.toSet());

        bookingMapper.updateEntity(req, booking);

        updatedItems.forEach(item -> item.setBooking(booking));

        return bookingMapper.toResponse(bookingRepo.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse get(Long id) {
        return bookingRepo.findById(id)
                .map(bookingMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> list() {
        return bookingRepo.findAll().stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepo.existsById(id)) {
            throw new NotFoundException("Booking %d not found".formatted(id));
        }
        bookingRepo.deleteById(id);
    }
}