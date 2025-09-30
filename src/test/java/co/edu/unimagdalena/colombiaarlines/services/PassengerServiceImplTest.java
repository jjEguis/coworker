package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    PassengerRepository passengerRepository;

    @InjectMocks
    PassengerServiceImpl service;

    @Test
    void shouldCreatePassenger() {
        var req = new PassengerCreateRequest("Ana", "ana@d.com",
                new PassengerProfileDto("+57", "CO"));

        Passenger passenger = PassengerMapper.toEntity(req);
        passenger.setId(1L);

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.fullName()).isEqualTo("Ana");
        assertThat(res.email()).isEqualTo("ana@d.com");
        verify(passengerRepository).save(any(Passenger.class));
    }

    

    @Test
    void shouldThrowWhenPassengerNotFoundById() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    void shouldListPassengers() {
        Passenger p1 = Passenger.builder().id(1L).fullName("Ana").email("ana@d.com").build();
        Passenger p2 = Passenger.builder().id(2L).fullName("Luis").email("luis@d.com").build();

        when(passengerRepository.findAll()).thenReturn(List.of(p1, p2));

        var list = service.list();

        assertThat(list.get(0).fullName()).isEqualTo("Ana");
        verify(passengerRepository).findAll();
    }


    @Test
    void shouldThrowWhenUpdatingNonExistentPassenger() {
        when(passengerRepository.findById(77L)).thenReturn(Optional.empty());

        var req = new PassengerUpdateRequest("X", "x@mail.com", null);

        assertThrows(NotFoundException.class, () -> service.update(77L, req));
    }

    @Test
    void shouldDeletePassenger() {
        when(passengerRepository.existsById(20L)).thenReturn(true);

        service.delete(20L);

        verify(passengerRepository).deleteById(20L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentPassenger() {
        when(passengerRepository.existsById(123L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.delete(123L));
    }
}