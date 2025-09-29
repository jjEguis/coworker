package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.*;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class SeatInventoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    SeatInventoryRepository seatInventoryRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Test
    @DisplayName("SeatInventory: encuentra por vuelo y cabina")
    void shouldFindSeatInventoryByFlightIdAndCabin() {
        // Given
        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV123").airline(airline).origin(origin).destination(destination).build());

        SeatInventory seatInventory = new SeatInventory();
        seatInventory.setFlight(flight);
        seatInventory.setCabin(Cabin.ECONOMY);
        seatInventory.setTotalSeats(150);
        seatInventory.setAvailableSeats(100);

        // When
        seatInventoryRepository.save(seatInventory);
        Optional<SeatInventory> foundInventory = seatInventoryRepository.findSeatInventoriesByFlight_IdAndCabin(flight.getId(), Cabin.ECONOMY);

        // Then
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getAvailableSeats()).isEqualTo(100);
    }

    @Test
    @DisplayName("SeatInventory: verifica asientos disponibles")
    void shouldCheckAvailableSeats() {
        // Given
        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("CTG").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Flight flight = flightRepository.save(Flight.builder().number("AV456").airline(airline).origin(origin).destination(destination).build());

        SeatInventory seatInventory = new SeatInventory();
        seatInventory.setFlight(flight);
        seatInventory.setCabin(Cabin.BUSINESS);
        seatInventory.setTotalSeats(20);
        seatInventory.setAvailableSeats(5);
        seatInventoryRepository.save(seatInventory);

        // When
        boolean areSeatsAvailable = seatInventoryRepository.availableSeats(flight.getId(), Cabin.BUSINESS, 4);
        boolean areSeatsNotAvailable = seatInventoryRepository.availableSeats(flight.getId(), Cabin.BUSINESS, 6);

        // Then
        assertThat(areSeatsAvailable).isTrue();
        assertThat(areSeatsNotAvailable).isFalse();
    }
}