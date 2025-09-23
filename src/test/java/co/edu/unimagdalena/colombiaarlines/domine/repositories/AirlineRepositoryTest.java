package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

public class AirlineRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirlineRepository airlineRepo;

    @Test
    @DisplayName("Airline: encuentra por codigo ")
    void shouldFindByCode() {
        //Given

        airlineRepo.save(Airline.builder().code("AV").build());

        //When / Then
        assertThat(airlineRepo.findAirlinesByCode("AV").isPresent());
    }
}
