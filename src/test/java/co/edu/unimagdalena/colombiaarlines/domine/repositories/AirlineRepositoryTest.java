package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AirlineRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirlineRepository airlineRepository; // <--- Asegúrate de que esta variable tenga el nombre correcto

    @Test
    @DisplayName("Airline: encuentra aerolínea por código")
    void shouldFindByCode() {
        // Given
        Airline airline = Airline.builder().code("AV").name("Avianca").build();
        airlineRepository.save(airline); // <--- Llama a la variable inyectada

        // When
        Optional<Airline> found = airlineRepository.findByCode("AV");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Avianca");
    }
}