package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerProfileRepository passengerProfileRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void shouldCreatePassengerWithProfile() {
        // Given
        PassengerProfileDto profileDto = new PassengerProfileDto("123456789", "CO");
        PassengerCreateRequest request = new PassengerCreateRequest(
                "Juan Pérez", "juan@email.com", profileDto
        );

        PassengerProfile savedProfile = PassengerProfile.builder()
                .id(100L)
                .phone("123456789")
                .countryCode("CO")
                .build();

        Passenger passenger = Passenger.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan@email.com")
                .passengerProfile(savedProfile)
                .build();

        when(passengerProfileRepository.save(any(PassengerProfile.class))).thenReturn(savedProfile);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        // When
        PassengerResponse result = passengerService.create(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("Juan Pérez");
        assertThat(result.profile().phone()).isEqualTo("123456789");

        verify(passengerProfileRepository).save(any(PassengerProfile.class));
        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    void shouldCreatePassengerWithoutProfile() {
        // Given
        PassengerCreateRequest request = new PassengerCreateRequest(
                "Ana López", "ana@email.com", null
        );

        Passenger passenger = Passenger.builder()
                .id(2L)
                .fullName("Ana López")
                .email("ana@email.com")
                .passengerProfile(null)
                .build();

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        // When
        PassengerResponse result = passengerService.create(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.fullName()).isEqualTo("Ana López");
        assertThat(result.profile()).isNull();

        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerProfileRepository, never()).save(any());
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

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        // When
        PassengerResponse result = passengerService.get(passengerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("María Gómez");
        assertThat(result.profile().phone()).isEqualTo("987654321");

        verify(passengerRepository).findById(passengerId);
    }

    @Test
    void shouldListAllPassengers() {
        // Given
        PassengerProfile profile1 = PassengerProfile.builder().phone("111111111").countryCode("CO").build();
        PassengerProfile profile2 = PassengerProfile.builder().phone("222222222").countryCode("MX").build();

        Passenger passenger1 = Passenger.builder().id(1L).fullName("Juan Pérez").email("juan@email.com").passengerProfile(profile1).build();
        Passenger passenger2 = Passenger.builder().id(2L).fullName("Ana López").email("ana@email.com").passengerProfile(profile2).build();

        when(passengerRepository.findAll()).thenReturn(List.of(passenger1, passenger2));

        // When
        List<PassengerResponse> result = passengerService.list();

        // Then
        assertThat(result).extracting("id").isIn(1L, 2L);
        assertThat(result).extracting("fullName").isIn("Juan Pérez", "Ana López");

        verify(passengerRepository).findAll();
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

        PassengerProfile savedProfile = PassengerProfile.builder()
                .id(100L)
                .phone("987654321")
                .countryCode("US")
                .build();

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerProfileRepository.save(any(PassengerProfile.class))).thenReturn(savedProfile);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(existingPassenger);

        // When
        PassengerResponse result = passengerService.update(passengerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(passengerRepository).findById(passengerId);
        verify(passengerProfileRepository).save(any(PassengerProfile.class));
        verify(passengerRepository).save(existingPassenger);
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

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerProfileRepository.save(any(PassengerProfile.class))).thenReturn(existingProfile);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(existingPassenger);

        // When
        PassengerResponse result = passengerService.update(passengerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(passengerRepository).findById(passengerId);
        verify(passengerProfileRepository).save(existingProfile);
        verify(passengerRepository).save(existingPassenger);
    }

    @Test
    void shouldDeletePassenger() {
        // Given
        Long passengerId = 1L;
        PassengerProfile profile = PassengerProfile.builder().phone("123456789").countryCode("CO").build();
        Passenger passenger = Passenger.builder().id(passengerId).fullName("Juan Pérez").email("juan@email.com").passengerProfile(profile).build();

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        // When
        passengerService.delete(passengerId);

        // Then
        verify(passengerRepository).findById(passengerId);
        verify(passengerRepository).delete(passenger);
    }

    @Test
    void shouldUpdatePassengerWithoutProfileChanges() {
        // Given
        Long passengerId = 1L;
        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(
                "Carlos Rodríguez", "carlos@email.com", null // Sin cambios en profile
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

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(existingPassenger);

        // When
        PassengerResponse result = passengerService.update(passengerId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(passengerRepository).findById(passengerId);
        verify(passengerProfileRepository, never()).save(any());
        verify(passengerRepository).save(existingPassenger);
    }
}