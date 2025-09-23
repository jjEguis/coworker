package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FlightRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
        airlineRepository.deleteAll();
        airportRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("Flight: lista vuelos por nombre de aerolínea")
    void shouldFindFlightsByAirlineName() {
        // Given
        Airline avianca = airlineRepository.save(Airline.builder().code("AV").name("Avianca").build());
        Airline latam = airlineRepository.save(Airline.builder().code("LA").name("LATAM").build());
        Airport bogota = airportRepository.save(Airport.builder().code("BOG").build());
        Airport medellin = airportRepository.save(Airport.builder().code("MDE").build());

        flightRepository.save(Flight.builder().number("AV123").airline(avianca).origin(bogota).destination(medellin).departureTime(OffsetDateTime.now()).arrivalTime(OffsetDateTime.now().plusHours(1)).build());
        flightRepository.save(Flight.builder().number("AV456").airline(avianca).origin(medellin).destination(bogota).departureTime(OffsetDateTime.now()).build());
        flightRepository.save(Flight.builder().number("LA789").airline(latam).origin(bogota).destination(medellin).departureTime(OffsetDateTime.now()).build());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        List<Flight> flights = flightRepository.findByAirlineName("Avianca", pageable);

        // Then
        assertThat(flights).hasSize(2);
        assertThat(flights).allMatch(f -> f.getAirline().getName().equals("Avianca"));
    }

    @Test
    @DisplayName("Flight: busca vuelos por origen, destino y fecha, con paginación")
    @Transactional
    void shouldFindFlightsByOriginAndDestinationAndTimeRange() {
        // Given
        Airport bogota = airportRepository.save(Airport.builder().code("BOG").build());
        Airport madrid = airportRepository.save(Airport.builder().code("MAD").build());
        Airport medellin = airportRepository.save(Airport.builder().code("MDE").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());

        OffsetDateTime from = OffsetDateTime.now();
        OffsetDateTime to = from.plusHours(2);

        flightRepository.save(Flight.builder().origin(bogota).destination(madrid).departureTime(from.plusMinutes(30)).airline(airline).build());
        flightRepository.save(Flight.builder().origin(bogota).destination(madrid).departureTime(from.plusHours(1)).airline(airline).build());
        flightRepository.save(Flight.builder().origin(bogota).destination(medellin).departureTime(from.plusHours(1)).airline(airline).build());
        flightRepository.save(Flight.builder().origin(bogota).destination(madrid).departureTime(to.plusHours(1)).airline(airline).build());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Flight> flightsPage = flightRepository.findFlightByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween("BOG", "MAD", from, to, pageable);

        // Then
        assertThat(flightsPage.getTotalElements()).isEqualTo(2);
        List<Flight> flights = flightsPage.getContent();
        assertThat(flights).hasSize(2);
        assertThat(flights).allMatch(f -> f.getOrigin().getCode().equals("BOG") && f.getDestination().getCode().equals("MAD"));
    }

    @Test
    @DisplayName("Flight: busca vuelos con asociaciones precargadas")
    @Transactional
    void shouldSearchFlightWithAllAssociations() {
        // Given
        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("JFK").build());
        Airline airline = airlineRepository.save(Airline.builder().code("AA").build());
        Tag tag = tagRepository.save(Tag.builder().name("promoción").build());

        Flight flight = Flight.builder()
                .number("AA100")
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(5))
                .build();
        flight.addTag(tag);
        flightRepository.save(flight);

        // When
        List<Flight> flights = flightRepository.searchFlight(origin, destination, OffsetDateTime.now().minusHours(1), OffsetDateTime.now().plusHours(1));

        // Then
        assertThat(flights).isNotEmpty();
        Flight foundFlight = flights.get(0);
        assertThat(foundFlight.getAirline()).isNotNull();
        assertThat(foundFlight.getOrigin()).isNotNull();
        assertThat(foundFlight.getDestination()).isNotNull();
        assertThat(foundFlight.getTags()).isNotEmpty();
        assertThat(foundFlight.getTags().get(0).getName()).isEqualTo("promoción");
    }

    @Test
    @DisplayName("Flight: encuentra vuelos con todas las tags requeridas")
    @Transactional
    void shouldFindFlightsWithAllTags() {
        // Given
        Tag tag1 = tagRepository.save(Tag.builder().name("promo").build());
        Tag tag2 = tagRepository.save(Tag.builder().name("eco").build());
        Tag tag3 = tagRepository.save(Tag.builder().name("red-eye").build());

        Airline airline = airlineRepository.save(Airline.builder().code("AV").build());
        Airport origin = airportRepository.save(Airport.builder().code("BOG").build());
        Airport destination = airportRepository.save(Airport.builder().code("MAD").build());

        Flight flight1 = Flight.builder().number("AV101").airline(airline).origin(origin).destination(destination).departureTime(OffsetDateTime.now()).build();
        flight1.addTag(tag1);
        flight1.addTag(tag2);
        flightRepository.save(flight1);

        Flight flight2 = Flight.builder().number("LA202").airline(airline).origin(origin).destination(destination).departureTime(OffsetDateTime.now()).build();
        flight2.addTag(tag1);
        flight2.addTag(tag2);
        flight2.addTag(tag3);
        flightRepository.save(flight2);

        Flight flight3 = Flight.builder().number("IB303").airline(airline).origin(origin).destination(destination).departureTime(OffsetDateTime.now()).build();
        flight3.addTag(tag1);
        flightRepository.save(flight3);

        // When
        List<Flight> foundFlights = flightRepository.findFlightsWithAllTags(Arrays.asList("promo", "eco"), 2);
        Set<String> flightNumbers = foundFlights.stream().map(Flight::getNumber).collect(Collectors.toSet());

        // Then
        assertThat(foundFlights).hasSize(2);
        assertThat(flightNumbers).containsExactlyInAnyOrder("AV101", "LA202");
    }
}