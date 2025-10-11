package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirportServiceImplTest {

    @Mock
    private AirportRepository repo;
    @InjectMocks
    private AirportServiceImpl service;

    @Test
    void shouldCreateAndReturnResponseDto(){
        var req = new AirportCreateRequest("BOG","El Dorado","BOGOTA");
        when(repo.save(any())).thenAnswer(inv -> {
            Airport a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.code()).isEqualTo("BOG");
        assertThat(res.name()).isEqualTo("El Dorado");
        assertThat(res.city()).isEqualTo("BOGOTA");
        verify(repo).save(any(Airport.class));

    }

    @Test
    void shouldGetAirportById() {
        Airport airport = Airport.builder()
                .id(2L).code("BOG").name("El Dorado").city("Bogotá").build();
        when(repo.findById(2L)).thenReturn(Optional.of(airport));

        var res = service.getById(2L);

        assertThat(res.id()).isEqualTo(2L);
        assertThat(res.code()).isEqualTo("BOG");
        assertThat(res.name()).isEqualTo("El Dorado");
        verify(repo).findById(2L);
    }

    @Test
    void shouldGetAirportByCode() {
        Airport airport = Airport.builder()
                .id(3L).code("MDE").name("José María Córdova").city("Medellín").build();
        when(repo.findByCode("MDE")).thenReturn(Optional.of(airport));

        var res = service.getByCode("MDE");

        assertThat(res.id()).isEqualTo(3L);
        assertThat(res.code()).isEqualTo("MDE");
        assertThat(res.name()).isEqualTo("José María Córdova");
        assertThat(res.city()).isEqualTo("Medellín");
        verify(repo).findByCode("MDE");
    }

    @Test
    void shouldListAirports() {
        var airport1 = Airport.builder().id(1L).code("SMR").name("Simón Bolívar").city("Santa Marta").build();
        var airport2 = Airport.builder().id(2L).code("BOG").name("El Dorado").city("Bogotá").build();
        when(repo.findAll()).thenReturn(List.of(airport1, airport2));

        var res = service.list();

        assertThat(res).hasSize(2);
        assertThat(res.get(0).code()).isEqualTo("SMR");
        assertThat(res.get(1).code()).isEqualTo("BOG");
        verify(repo).findAll();
    }

    @Test
    void shouldDeleteAirline() {
        service.delete(7L);

        verify(repo).deleteById(7L);
    }
}
