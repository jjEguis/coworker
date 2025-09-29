package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AirportRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirportRepository airportRepo;

    @Test
    @DisplayName("Airport: buscar por code")
    public void shouldFindAirportByCode(){

        airportRepo.save(Airport.builder().code("BOG").build());


        assertThat(airportRepo.findAirportByCode("BOG").isPresent());
    }
}
