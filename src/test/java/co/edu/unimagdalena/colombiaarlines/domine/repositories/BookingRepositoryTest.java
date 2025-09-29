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
        Passenger passenger = passengerRepository.save(Passenger.builder().email("juan@demo.com").fullName("Juan Perez").build());
        //

        bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now().minusDays(2)).build());
        bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now().minusDays(1)).build());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        // When
        Page<Booking> bookingsPage = bookingRepository.findBookingByPassenger_EmailOrderByCreatedAtDesc("juan@demo.com", pageable);

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
        Passenger passenger = passengerRepository.save(Passenger.builder().email("juan@demo.com").fullName("Juan Perez").build());
        //;
        Airport originAirport = airportRepository.save(Airport.builder().code("BOG").name("ElDorado").city("Bogota").build());
        Airport destinationAirport = airportRepository.save(Airport.builder().code("JFK").name("John F Kenedy Air").city("Miami").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").name("Avianca").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").departureTime(OffsetDateTime.now().minusDays(1))
                        .arrivalTime(OffsetDateTime.now()).airline(airline).origin(originAirport).destination(destinationAirport).build());

        Booking booking = bookingRepository.save(Booking.builder().passenger(passenger).createdAt(OffsetDateTime.now()).build());
        // --- CAMBIO IMPORTANTE AQUÍ ---
        // booking.addItem() es suficiente. NO NECESITAS LLAMAR A bookingRepository.save(booking) de nuevo.
        booking.addItem(BookingItem.builder().flight(flight).price(BigDecimal.valueOf(500.00)).segmentOrder(3).build());
        // Quita la siguiente línea:
        // bookingRepository.save(booking);

        // Cuando el test termine y la transacción se haga rollback, los cambios en 'booking'
        // (incluyendo la adición del item) serán detectados y persistidos/deshechos correctamente.

        // When
        Booking fetchedBooking = bookingRepository.searchBooking(booking.getId());

        // Then
        assertThat(fetchedBooking).isNotNull();
        assertThat(fetchedBooking.getPassenger()).isNotNull();
        assertThat(fetchedBooking.getItems()).isNotEmpty();
        assertThat(fetchedBooking.getItems().get(0).getFlight()).isNotNull();
    }
}