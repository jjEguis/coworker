package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.SeatInventoryDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.services.SeatInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatInventoryController.class)
class SeatInventoryControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean SeatInventoryService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new SeatInventoryCreateRequest(Cabin.ECONOMY, 180, 150, 1L);
        var resp = new SeatInventoryResponse(1L, Cabin.ECONOMY, 180, 150, 1L);

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/seat-inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/seat-inventories/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(180));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var inventories = List.of(
                new SeatInventoryResponse(1L, Cabin.ECONOMY, 180, 150, 1L),
                new SeatInventoryResponse(2L, Cabin.BUSINESS, 30, 25, 1L)
        );
        when(service.list()).thenReturn(inventories);

        mvc.perform(get("/api/seat-inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$[1].cabin").value("BUSINESS"));
    }

    @Test
    void findByFlightAndCabin_shouldReturn200() throws Exception {
        var resp = new SeatInventoryResponse(1L, Cabin.ECONOMY, 180, 150, 5L);
        when(service.findByFlightAndCabin(5L, Cabin.ECONOMY)).thenReturn(Optional.of(resp));

        mvc.perform(get("/api/seat-inventories/flight/5/cabin/ECONOMY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId").value(5))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.availableSeats").value(150));
    }

    @Test
    void findByFlightAndCabin_shouldReturn404WhenNotFound() throws Exception {
        when(service.findByFlightAndCabin(99L, Cabin.ECONOMY)).thenReturn(Optional.empty());

        mvc.perform(get("/api/seat-inventories/flight/99/cabin/ECONOMY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkAvailability_shouldReturn200WithTrue() throws Exception {
        when(service.availableSeats(5L, Cabin.ECONOMY, 10)).thenReturn(true);

        mvc.perform(get("/api/seat-inventories/flight/5/cabin/ECONOMY/available")
                        .param("min", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkAvailability_shouldReturn200WithFalse() throws Exception {
        when(service.availableSeats(5L, Cabin.BUSINESS, 50)).thenReturn(false);

        mvc.perform(get("/api/seat-inventories/flight/5/cabin/BUSINESS/available")
                        .param("min", "50"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void checkAvailability_shouldUseDefaultMinValue() throws Exception {
        when(service.availableSeats(5L, Cabin.ECONOMY, 1)).thenReturn(true);

        mvc.perform(get("/api/seat-inventories/flight/5/cabin/ECONOMY/available"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        verify(service).availableSeats(5L, Cabin.ECONOMY, 1);
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new SeatInventoryUpdateRequest(180, 140);
        var resp = new SeatInventoryResponse(1L, Cabin.ECONOMY, 180, 140, 1L);

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/seat-inventories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeats").value(180))
                .andExpect(jsonPath("$.availableSeats").value(140));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/seat-inventories/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}