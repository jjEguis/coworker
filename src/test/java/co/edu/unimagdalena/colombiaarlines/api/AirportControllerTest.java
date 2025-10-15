package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.AirportService;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean AirportService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new AirportCreateRequest("BOG", "El Dorado", "Bogotá");
        var resp = new AirportResponse(1L, "BOG", "El Dorado", "Bogotá", List.of(), List.of());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/airports/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        when(service.getById(1L)).thenReturn(
                new AirportResponse(1L, "BOG", "El Dorado", "Bogotá", List.of(), List.of())
        );

        mvc.perform(get("/api/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("Bogotá"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(service.getById(99L)).thenThrow(new NotFoundException("Airport 99 not found"));

        mvc.perform(get("/api/airports/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airport 99 not found"));
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        when(service.getByCode("BOG")).thenReturn(
                new AirportResponse(1L, "BOG", "El Dorado", "Bogotá", List.of(), List.of())
        );

        mvc.perform(get("/api/airports/code/BOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("El Dorado"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var airports = List.of(
                new AirportResponse(1L, "BOG", "El Dorado", "Bogotá", List.of(), List.of()),
                new AirportResponse(2L, "MDE", "José María Córdova", "Medellín", List.of(), List.of())
        );
        when(service.list()).thenReturn(airports);

        mvc.perform(get("/api/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new AirportUpdateRequest("El Dorado International", "Bogotá D.C.");
        var resp = new AirportResponse(1L, "BOG", "El Dorado International", "Bogotá D.C.", List.of(), List.of());

        when(service.getById(1L)).thenReturn(resp);

        mvc.perform(put("/api/airports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("El Dorado International"))
                .andExpect(jsonPath("$.city").value("Bogotá D.C."));
        
        verify(service).update(eq(1L), any());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/airports/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}