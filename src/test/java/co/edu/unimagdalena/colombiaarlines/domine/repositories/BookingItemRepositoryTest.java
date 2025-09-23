package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;

import static org.assertj.core.api.Assertions.assertThat;

class BookingItemRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    BookingItemRepository bookingItemRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Test
    @DisplayName("BookingItem: lista items de una reserva ordenados por segmento")
    void shouldFindItemsByBookingIdOrderBySegmentOrder() {
        // Given
        Passenger passenger = passengerRepository.save(new Passenger());
        Booking booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());

        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight1 = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(origin).destination(destination).build());
        Flight flight2 = flightRepository.save(Flight.builder().number("AV456").airline(airline).origin(destination).destination(origin).build());

        BookingItem item1 = bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight2).segmentOrder(2).build());
        BookingItem item2 = bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight1).segmentOrder(1).build());

        // When
        List<BookingItem> items = bookingItemRepository.findBookingItemByBookingIdOrderBySegmentOrder(booking.getId());

        // Then
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getSegmentOrder()).isEqualTo(1);
        assertThat(items.get(1).getSegmentOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("BookingItem: calcula el total de la reserva")
    void shouldCalculateTotalBookingPrice() {
        // Given
        Passenger passenger = passengerRepository.save(new Passenger());
        Booking booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());

        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(origin).destination(destination).build());

        bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight).price(BigDecimal.valueOf(100.50)).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight).price(BigDecimal.valueOf(250.75)).build());

        // When
        BigDecimal total = bookingItemRepository.getTotalPrice(booking.getId());

        // Then
        assertThat(total).isEqualTo(BigDecimal.valueOf(351.25));
    }

    @Test
    @DisplayName("BookingItem: cuenta asientos vendidos para un vuelo y cabina")
    void shouldCountSoldSeatsForFlightAndCabin() {
        // Given
        Passenger passenger1 = passengerRepository.save(new Passenger());
        Passenger passenger2 = passengerRepository.save(new Passenger());
        Booking booking1 = bookingRepository.save(Booking.builder().passenger(passenger1).createdAt(OffsetDateTime.now()).build());
        Booking booking2 = bookingRepository.save(Booking.builder().passenger(passenger2).createdAt(OffsetDateTime.now()).build());

        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(origin).destination(destination).build());

        bookingItemRepository.save(BookingItem.builder().booking(booking1).flight(flight).cabin(Cabin.ECONOMY).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking2).flight(flight).cabin(Cabin.ECONOMY).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking2).flight(flight).cabin(Cabin.BUSINESS).build());

        // When
        long economySeats = bookingItemRepository.seatsSold(flight.getId(), 0L); // cabin.ECONOMY
        long businessSeats = bookingItemRepository.seatsSold(flight.getId(), 2L); // cabin.BUSSNISES

        // Then
        assertThat(economySeats).isEqualTo(2);
        assertThat(businessSeats).isEqualTo(1);
    }
}