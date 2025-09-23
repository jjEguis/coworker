package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Test
    @DisplayName("Booking: pagina las reservas de un pasajero")
    @Transactional
    void shouldFindBookingsByPassengerEmail() {
        // Given
        Passenger passenger = passengerRepository.save(new Passenger());
        passenger.setEmail("juan@demo.com");

        bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now().minusDays(2)).build());
        bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now().minusDays(1)).build());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        // When
        Page<Booking> bookingsPage = bookingRepository.findBookingByPassenger_EmailOrderByCreatedAtDesc("JUAN@DEMO.COM", pageable);

        // Then
        assertThat(bookingsPage.getTotalElements()).isEqualTo(2);
        List<Booking> bookings = bookingsPage.getContent();
        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getCreatedAt()).isAfter(bookings.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("Booking: trae una reserva por ID con asociaciones precargadas")
    @Transactional
    void shouldFetchBookingWithAssociations() {
        // Given
        Passenger passenger = passengerRepository.save(new Passenger());
        Airport originAirport = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destinationAirport = airportRepository.save(Airport.builder().code("JFK").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(originAirport).destination(destinationAirport).build());

        Booking booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());
        booking.addItem(BookingItem.builder().flight(flight).price(BigDecimal.valueOf(500.00)).build());
        bookingRepository.save(booking);

        // When
        Booking fetchedBooking = bookingRepository.searchBooking(booking.getId());

        // Then
        assertThat(fetchedBooking).isNotNull();
        assertThat(fetchedBooking.getPassenger()).isNotNull();
        assertThat(fetchedBooking.getItems()).isNotEmpty();
        assertThat(fetchedBooking.getItems().get(0).getFlight()).isNotNull();
    }
}