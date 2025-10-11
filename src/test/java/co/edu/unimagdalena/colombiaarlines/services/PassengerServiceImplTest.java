package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerProfileRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepo;

    @Mock
    private PassengerProfileRepository passengerProfileRepo;

    @InjectMocks
    private PassengerServiceImpl service;

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new PassengerCreateRequest("Ana", "ana@d.com", new PassengerProfileDto("+57", "CO"));
        when(passengerRepo.save(any())).thenAnswer(inv -> {
            Passenger p = inv.getArgument(0);
            p.setId(11L);
            return p;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(11L);
        assertThat(res.email()).isEqualTo("ana@d.com");
        verify(passengerRepo).save(any(Passenger.class));
    }


    @Test
    void shouldGetPassengerById() {
        // Given
        Long passengerId = 1L;
        PassengerProfile profile = PassengerProfile.builder()
                .phone("987654321")
                .countryCode("US")
                .build();

        Passenger passenger = Passenger.builder()
                .id(passengerId)
                .fullName("María Gómez")
                .email("maria@email.com")
                .passengerProfile(profile)
                .build();

        when(passengerRepo.findById(passengerId)).thenReturn(Optional.of(passenger));

        // When
        PassengerResponse result = service.get(passengerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("María Gómez");
        assertThat(result.profile().phone()).isEqualTo("987654321");

        verify(passengerRepo).findById(passengerId);
    }

    @Test
    void shouldListAllPassengers() {
        // Given
        PassengerProfile profile1 = PassengerProfile.builder().phone("111111111").countryCode("CO").build();
        PassengerProfile profile2 = PassengerProfile.builder().phone("222222222").countryCode("MX").build();

        Passenger passenger1 = Passenger.builder().id(1L).fullName("Juan Pérez").email("juan@email.com").passengerProfile(profile1).build();
        Passenger passenger2 = Passenger.builder().id(2L).fullName("Ana López").email("ana@email.com").passengerProfile(profile2).build();

        when(passengerRepo.findAll()).thenReturn(List.of(passenger1, passenger2));

        // When
        List<PassengerResponse> result = service.list();

        // Then
        assertThat(result).extracting("id").containsExactly(1L, 2L);
        assertThat(result).extracting("fullName").containsExactly("Juan Pérez", "Ana López");

        verify(passengerRepo).findAll();
    }

    @Test
    void shouldUpdatePassengerWithNewProfile() {
        // Given
        Long passengerId = 1L;
        PassengerProfileDto updatedProfile = new PassengerProfileDto("987654321", "US");
        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                "Carlos Rodríguez", "carlos@email.com", updatedProfile
        );

        Passenger existingPassenger = Passenger.builder()
                .id(passengerId)
                .fullName("Juan Pérez")
                .email("juan@email.com")
                .passengerProfile(null) // Sin profile inicial
                .build();



        when(passengerRepo.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepo.save(any(Passenger.class))).thenReturn(existingPassenger);

        // When
        PassengerResponse result = service.update(passengerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(passengerRepo).findById(passengerId);
        verify(passengerRepo).save(existingPassenger);
    }

    @Test
    void shouldUpdatePassengerWithExistingProfile() {
        // Given
        Long passengerId = 1L;
        PassengerProfileDto updatedProfile = new PassengerProfileDto("999999999", "CA");
        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                "Carlos Rodríguez", "carlos@email.com", updatedProfile
        );

        PassengerProfile existingProfile = PassengerProfile.builder()
                .id(100L)
                .phone("123456789")
                .countryCode("CO")
                .build();

        Passenger existingPassenger = Passenger.builder()
                .id(passengerId)
                .fullName("Juan Pérez")
                .email("juan@email.com")
                .passengerProfile(existingProfile)
                .build();

        when(passengerRepo.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepo.save(any(Passenger.class))).thenReturn(existingPassenger);

        // When
        PassengerResponse result = service.update(passengerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(passengerRepo).findById(passengerId);
        verify(passengerRepo).save(existingPassenger);
    }

    @Test
    void shouldDeletePassenger() {
        // Given
        Long passengerId = 1L;
        PassengerProfile profile = PassengerProfile.builder().phone("123456789").countryCode("CO").build();
        Passenger passenger = Passenger.builder().id(passengerId).fullName("Juan Pérez").email("juan@email.com").passengerProfile(profile).build();

        when(passengerRepo.findById(passengerId)).thenReturn(Optional.of(passenger));

        // When
        service.delete(passengerId);

        // Then
        verify(passengerRepo).findById(passengerId);
        verify(passengerRepo).delete(passenger);
    }


}