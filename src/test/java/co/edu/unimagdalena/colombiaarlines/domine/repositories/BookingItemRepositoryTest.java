package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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

        var airline = airlineRepository.save(Airline.builder().code("AV").build());
        var airport = airportRepository.save(Airport.builder().build());
        if (airport.getId() == null) {
            throw new RuntimeException("No se encontro el airport");
        }
        var passenger = passengerRepository.save(Passenger.builder().fullName("pepito perez").email("sincorreo@ema").build());
        var booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());
        var flight = flightRepository.save(Flight.builder().airline(airline).origin(airport).destination(airport).build());
        var bookingItem = bookingItemRepository.save(BookingItem.builder()
                .cabin(Cabin.ECONOMY).price(new BigDecimal(2))
                .segmentOrder(1).booking(booking).flight(flight).build());

        var bookingItem1 = bookingItemRepository.save(BookingItem.builder()
                .cabin(Cabin.ECONOMY).price(new BigDecimal(3))
                .segmentOrder(2).booking(booking).flight(flight).build());

        bookingItemRepository.saveAll(List.of(bookingItem1, bookingItem));

        assertThat(bookingItemRepository.findBookingItemByBookingIdOrderBySegmentOrder(booking.getId()).get(0).getSegmentOrder()).isEqualTo(1);
        List<BookingItem> items = bookingItemRepository.findBookingItemByBookingIdOrderBySegmentOrder(booking.getId());
        for (BookingItem item : items) {
            System.out.println(item.toString());
        }
    }


    @Test
    @DisplayName("BookingItem: calcula el total de la reserva")
    void shouldCalculateTotalBookingPrice() {
        // Given
        var passenger = passengerRepository.save(Passenger.builder().build());
        Booking booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());

        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(origin).destination(destination).build());

        bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight).price(BigDecimal.valueOf(100.50)).segmentOrder(1).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking).flight(flight).price(BigDecimal.valueOf(250.75)).segmentOrder(2).build());

        // When
        BigDecimal total = bookingItemRepository.getTotalPrice(booking.getId());

        // Then
        assertThat(total).isEqualTo(BigDecimal.valueOf(351.25));
    }

    @Test
    @DisplayName("BookingItem: cuenta asientos vendidos para un vuelo y cabina")
    void shouldCountSoldSeatsForFlightAndCabin() {
        // Given
        var passenger1 = passengerRepository.save(Passenger.builder().fullName("Andres Tobias").email("andretb@test.com").build());
        var passenger2 = passengerRepository.save(Passenger.builder().fullName("Julian  Tobia").email("juliantb@test.com").build());



        var booking1 = bookingRepository.save(Booking.builder().passenger(passenger1).createdAt(OffsetDateTime.now()).build());
        var booking2 = bookingRepository.save(Booking.builder().passenger(passenger2).createdAt(OffsetDateTime.now()).build());

        var origin = airportRepository.save(Airport.builder().code("BOG").name("Girardot").city("Bogota city").build());
        var destination = airportRepository.save(Airport.builder().code("CTG").name("Catalinas Flies").city("Indias").build());
        var airline = airlineRepository.save(Airline.builder().code("AV").build());
        var flight = flightRepository.save(Flight.builder().number("AV123")
                .airline(airline).origin(origin).destination(destination).build());

        bookingItemRepository.save(BookingItem.builder().booking(booking1).flight(flight)
                .cabin(Cabin.ECONOMY).segmentOrder(1).price(BigDecimal.valueOf(300)).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking2).flight(flight)
                .cabin(Cabin.ECONOMY).segmentOrder(1).price(BigDecimal.valueOf(300)).build());
        bookingItemRepository.save(BookingItem.builder().booking(booking2).flight(flight)
                .cabin(Cabin.BUSINESS).segmentOrder(1).price(BigDecimal.valueOf(300)).build());

        // When
        long economySeats = bookingItemRepository.seatsSold(flight.getId(),Cabin.ECONOMY);
        long businessSeats = bookingItemRepository.seatsSold(flight.getId(),Cabin.BUSINESS);
        long premiumSeats = bookingItemRepository.seatsSold(flight.getId(),Cabin.PREMIUM);

        // Then
        assertThat(economySeats).isEqualTo(2);
        assertThat(businessSeats).isEqualTo(1);
    }
}