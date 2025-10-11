package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.services.AirlineService;
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

@WebMvcTest(AirlineController.class)
class AirlineControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean AirlineService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new AirlineCreateRequest("AV", "Avianca");
        var resp = new AirlineResponse(10L, "AV", "Avianca", List.of());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/airlines/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.code").value("AV"))
                .andExpect(jsonPath("$.name").value("Avianca"));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        when(service.get(5L)).thenReturn(new AirlineResponse(5L, "LA", "LATAM", List.of()));

        mvc.perform(get("/api/airlines/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.code").value("LA"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L)).thenThrow(new NotFoundException("Airline not found"));

        mvc.perform(get("/api/airlines/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airline not found"));
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        when(service.getByCode("AV")).thenReturn(new AirlineResponse(1L, "AV", "Avianca", List.of()));

        mvc.perform(get("/api/airlines/AV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AV"));
    }

    @Test
    void getByCode_shouldReturn404WhenNotFound() throws Exception {
        when(service.getByCode("XX")).thenThrow(new NotFoundException("Airline not found by code"));

        mvc.perform(get("/api/airlines/XX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airline not found by code"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var airlines = List.of(
                new AirlineResponse(1L, "AV", "Avianca", List.of()),
                new AirlineResponse(2L, "LA", "LATAM", List.of())
        );
        when(service.list()).thenReturn(airlines);

        mvc.perform(get("/api/airlines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("AV"))
                .andExpect(jsonPath("$[1].code").value("LA"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new AirlineUpdateRequest("Avianca S.A.", "AV");
        var resp = new AirlineResponse(3L, "AV", "Avianca S.A.", List.of());

        when(service.update(eq(3L), any())).thenReturn(resp);

        mvc.perform(patch("/api/airlines/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Avianca S.A."));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/airlines/3"))
                .andExpect(status().isNoContent());
        verify(service).delete(3L);
    }
}