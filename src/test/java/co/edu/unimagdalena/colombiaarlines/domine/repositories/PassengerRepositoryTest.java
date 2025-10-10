package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PassengerRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("Passenger: encuentra por email (ignore case)")
    void shouldFindByEmailIgnoreCase() {
        // Given
        Passenger passenger = new Passenger();
        passenger.setFullName("Sofia Hernandez");
        passenger.setEmail("sofia@demo.com");

        // When
        passengerRepository.save(passenger);
        Optional<Passenger> foundPassenger = passengerRepository.findByEmailIgnoreCase("SOFIA@DEMO.COM");

        // Then
        assertThat(foundPassenger).isPresent();
        assertThat(foundPassenger.get().getEmail()).isEqualTo("sofia@demo.com");
    }

    @Test
    @DisplayName("Passenger: encuentra por email (ignore case) y hace fetch del profile")
    void shouldFindByEmailIgnoreCaseAndFetchProfile() {
        // Given

        PassengerProfile profile = PassengerProfile.builder().phone("31250064").countryCode("CO").build();
        passengerProfileRepository.save(profile);

        Passenger passenger = new Passenger();
        passenger.setFullName("Carlos Gomez");
        passenger.setEmail("carlos@demo.com");
        passenger.setPassengerProfile(profile);
        passengerRepository.save(passenger);


        // When
        Optional<Passenger> foundPassenger = passengerRepository.findByEmailIgnoreCaseAndPassengerProfile("CARLOS@DEMO.COM");

        // Then
        assertThat(foundPassenger).isPresent();
        assertThat(foundPassenger.get().getEmail()).isEqualTo("carlos@demo.com");
        assertThat(foundPassenger.get().getPassengerProfile()).isNotNull();
        assertThat(foundPassenger.get().getPassengerProfile().getCountryCode()).isEqualTo("CO");
    }
}