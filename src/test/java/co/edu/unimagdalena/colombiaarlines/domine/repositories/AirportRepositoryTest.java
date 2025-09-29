package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class AirportRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    AirportRepository airportRepo;

    @Test
    @DisplayName("Airport: buscar por code")
    @Transactional
    public void shouldFindAirportByCode(){
        Airport airport = Airport.builder().code("BOG").build();
        airportRepo.save(airport);


        assertThat(airportRepo.findAirportByCode("BOG").getCode().equals("BOG")).isTrue();
    }
}
