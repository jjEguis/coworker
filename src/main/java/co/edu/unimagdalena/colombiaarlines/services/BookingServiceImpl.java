package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.BookingUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemCreateRequest;
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

    @Override
    public BookingResponse create(BookingCreateRequest req) {
        // 1. LÓGICA DE NEGOCIO: Buscar y validar el pasajero principal
        Passenger passenger = passengerRepo.findById(req.passengerId())
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(req.passengerId())));
        
        // 2. LÓGICA DE NEGOCIO: Procesar y validar cada Booking Item
        List<BookingItem> items = req.items().stream()
                .map(this::createBookingItem)
                .toList();

        // 3. Mapear DTO -> Entidad. Se asume que BookingCreateRequest no tiene 'createdAt', se asigna aquí.
        Booking booking = BookingMapper.toEntity(req, passenger, items);
        booking.setCreatedAt(OffsetDateTime.now());

        // 4. Establecer la relación bidireccional (BookingItem -> Booking)
        items.forEach(item -> item.setBooking(booking));

        // 5. Persistir y Mapear a Respuesta
        return BookingMapper.toResponse(bookingRepo.save(booking));
    }

    /**
     * Para crear y validar un solo BookingItem.
     */
    private BookingItem createBookingItem(BookingItemCreateRequest req) {

        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight %d not found for booking item".formatted(req.flightId())));

        // Lógica de validación (e.g.: disponibilidad de asientos) irí

        BookingItem item = BookingItemMapper.toEntity(req);
        item.setFlight(flight);
        return item;
    }


    @Override
    @Transactional(readOnly = true)
    public BookingResponse get(Long id) {
        return bookingRepo.findById(id)
                .map(BookingMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));
    }


    // Operación LIST, UPDATE y DELETE le falta
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> list() {
        return bookingRepo.findAll().stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse update(Long id, BookingUpdateRequest req) {
        //Busca la entidad Booking existente.
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(id)));

        List<BookingItem> updatedItems = req.items().stream()
                .map(this::createBookingItem)
                .toList();

        BookingMapper.updateEntity(booking, req, updatedItems);

        updatedItems.forEach(item -> item.setBooking(booking));

        // 5. Persistir y Mapear la Respuesta.
        return BookingMapper.toResponse(bookingRepo.save(booking));
    }


    @Override
    public void delete(Long id) {
        if (!bookingRepo.existsById(id)) {
            throw new NotFoundException("Booking %d not found".formatted(id));
        }
        bookingRepo.deleteById(id);
    }
}