package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AirportRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirportRepository airportRepo;

    @Test
    @DisplayName("Airport: buscar por code")
    @Transactional
    public void shouldFindAirportByCode(){
        Airport airport = Airport.builder().code("BOG").build();
        airportRepo.save(airport);


        assertThat(airportRepo.findByCode("BOG").isPresent()).isTrue();
    }
}
