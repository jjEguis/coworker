package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirlineRepository;
import org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirlineServiceImplTest {

    @Mock
    AirlineRepository repo;
    @InjectMocks
    AirlineServiceImpl service;

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new AirlineCreateRequest("AV","Avianca");
        when(repo.save(any())).thenAnswer(inv -> {
            Airline a = inv.getArgument(0);
            a.setId(11L);
            return a;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(11L);
        assertThat(res.name()).isEqualTo("Avianca");
        assertThat(res.code()).isEqualTo("AV");
        verify(repo).save(any(Airline.class));

    }

    @Test
    void shouldGetAirlineById() {
        Airline a = Airline.builder().id(10L).code("LA").name("LATAM").build();
        when(repo.findById(10L)).thenReturn(Optional.of(a));

        var res = service.get(10L);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.code()).isEqualTo("LA");
        assertThat(res.name()).isEqualTo("LATAM");
        verify(repo).findById(10L);
    }

    @Test
    void shouldGetAirlineByCode() {
        Airline a = Airline.builder().id(5L).code("IB").name("Iberia").build();
        when(repo.findByCode("IB")).thenReturn(Optional.of(a));

        var res = service.getByCode("IB");

        assertThat(res.id()).isEqualTo(5L);
        assertThat(res.code()).isEqualTo("IB");
        assertThat(res.name()).isEqualTo("Iberia");
        verify(repo).findByCode("IB");
    }

    @Test
    void shouldListAirlines() {
        Airline a1 = Airline.builder().id(1L).code("AV").name("Avianca").build();
        Airline a2 = Airline.builder().id(2L).code("LA").name("LATAM").build();

        when(repo.findAll()).thenReturn(List.of(a1, a2));

        var res = service.list();

        assertThat(res).hasSize(2);
        assertThat(res).extracting("code").containsExactly("AV", "LA");
        verify(repo).findAll();
    }

    @Test
    void shouldUpdate() {
        // Given
        Long airlineId = 1L;
        AirlineUpdateRequest updateRequest = new AirlineUpdateRequest(
                "LATAM Airlines Group",
                "LA"
        );

        Airline existingAirline = Airline.builder()
                .id(airlineId)
                .name("LAN Airlines")
                .code("LAN")
                .build();

        Airline updatedAirline = Airline.builder()
                .id(airlineId)
                .name("LATAM Airlines Group")
                .code("LA")
                .build();

        when(repo.findById(airlineId)).thenReturn(Optional.of(existingAirline));
        when(repo.save(any(Airline.class))).thenReturn(updatedAirline);

        // When
        AirlineResponse result = service.update(airlineId, updateRequest);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("LATAM Airlines Group");
        assertThat(result.code()).isEqualTo("LA");
    }

    @Test
    void shouldDeleteAirline() {
        service.delete(7L);

        verify(repo).deleteById(7L);
    }

}
