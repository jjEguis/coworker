package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AirlineRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirlineRepository airlineRepository;

    @Test
    @DisplayName("Airline: encuentra aerolínea por código")
    void shouldFindByCode() {


        Airline airline = Airline.builder().code("AV").name("Avianca").build();
        airlineRepository.save(airline);

        // When
        Optional<Airline> found = airlineRepository.findByCode("AV");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Avianca");
    }
}