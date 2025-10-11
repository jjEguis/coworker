package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.PassengerServiceImpl;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerMapper;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerProfileMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    PassengerRepository passengerRepository;
    @Mock
    PassengerMapper passengerMapper;
    @Mock
    PassengerProfileMapper profileMapper; // Mockeado para simular la actualización del perfil

    @InjectMocks
    PassengerServiceImpl service; 

    private final Long PASSENGER_ID = 1L;
    private final String FULL_NAME = "Juan Pérez";
    private final String EMAIL = "juan@perez.com";
    private final String PHONE = "3001234567";
    private final String COUNTRY_CODE = "+57";

    private PassengerProfileDto profileDto() {
        return new PassengerProfileDto(PHONE, COUNTRY_CODE);
    }
    private PassengerResponse passengerResponse() {
        return new PassengerResponse(PASSENGER_ID, FULL_NAME, EMAIL, profileDto());
    }
    private Passenger passengerEntity() {
        return Passenger.builder()
                .id(PASSENGER_ID)
                .fullName(FULL_NAME)
                .email(EMAIL)
                .passengerProfile(passengerProfileEntity())
                .build();
    }
    private PassengerProfile passengerProfileEntity() {
        return PassengerProfile.builder().phone(PHONE).countryCode(COUNTRY_CODE).build();
    }


    @Test
    void shouldCreatePassengerAndReturnResponseDto() {
        var req = new PassengerCreateRequest(FULL_NAME, EMAIL, profileDto());
        var passengerToSave = Passenger.builder().fullName(FULL_NAME).email(EMAIL).passengerProfile(passengerProfileEntity()).build();
        var savedPassenger = passengerEntity();
        var expectedResponse = passengerResponse();

        when(passengerMapper.toEntity(req)).thenReturn(passengerToSave);

        when(passengerRepository.save(passengerToSave)).thenAnswer(inv -> {
            Passenger p = inv.getArgument(0);
            p.setId(PASSENGER_ID);
            assertThat(p.getPassengerProfile().getPassenger()).isEqualTo(p);
            return p;
        });

        when(passengerMapper.toResponse(savedPassenger)).thenReturn(expectedResponse);

        var result = service.create(req);

        assertThat(result).isEqualTo(expectedResponse);
        verify(passengerRepository).save(passengerToSave);
    }

    @Test
    void shouldFindByIdAndReturnResponseDto() {
        var foundPassenger = passengerEntity();
        var expectedResponse = passengerResponse();

        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(foundPassenger));
        when(passengerMapper.toResponse(foundPassenger)).thenReturn(expectedResponse);

        var result = service.get(PASSENGER_ID);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingNonExistentId() {
        when(passengerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(PASSENGER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(PASSENGER_ID.toString());
    }

    @Test
    void shouldReturnListOfResponseDtos() {
        var p1 = passengerEntity();
        var p2 = Passenger.builder().id(2L).fullName("Ana").email("ana@a.com").build();
        var passengers = List.of(p1, p2);
        
        var res1 = passengerResponse();
        var res2 = new PassengerResponse(2L, "Ana", "ana@a.com", null);
        var expectedList = List.of(res1, res2);

        when(passengerRepository.findAll()).thenReturn(passengers);
        when(passengerMapper.toResponse(p1)).thenReturn(res1);
        when(passengerMapper.toResponse(p2)).thenReturn(res2);

        var result = service.list();
        assertThat(result).hasSize(2).containsExactlyElementsOf(expectedList);
    }

    @Test
    void shouldUpdatePassengerAndProfileAndReturnResponseDto() {
        var newFullName = "Juan Pérez Actualizado";
        var newPhone = "3109876543";
        var req = new PassengerUpdateRequest(newFullName, EMAIL, new PassengerProfileDto(newPhone, COUNTRY_CODE));
        
        var existingPassenger = passengerEntity();
        var existingProfile = existingPassenger.getPassengerProfile();

        var updatedPassenger = Passenger.builder().id(PASSENGER_ID).fullName(newFullName).email(EMAIL).passengerProfile(existingProfile).build();
        var expectedResponse = new PassengerResponse(PASSENGER_ID, newFullName, EMAIL, new PassengerProfileDto(newPhone, COUNTRY_CODE));

        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(existingPassenger)).thenReturn(updatedPassenger);
        when(passengerMapper.toResponse(updatedPassenger)).thenReturn(expectedResponse);

        var result = service.update(PASSENGER_ID, req);

        verify(passengerMapper).updateEntity(req, existingPassenger);

        verify(profileMapper).updateEntity(req.profile(), existingProfile);
        
        assertThat(result.fullName()).isEqualTo(newFullName);
        assertThat(result.profile().phone()).isEqualTo(newPhone);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentId() {
        var req = new PassengerUpdateRequest("Test", "a@b.com", null);
        when(passengerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(PASSENGER_ID, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(PASSENGER_ID.toString());
        
        verify(passengerRepository, never()).save(any());
        verify(passengerMapper, never()).updateEntity(any(), any());
    }

    @Test
    void shouldDeletePassengerSuccessfully() {
        when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(true);

        service.delete(PASSENGER_ID);

        verify(passengerRepository).deleteById(PASSENGER_ID);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentId() {
        when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(PASSENGER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(PASSENGER_ID.toString());
        
        verify(passengerRepository, never()).deleteById(anyLong());
    }
}