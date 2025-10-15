package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.FlightService;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean FlightService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var req = new FlightCreateRequest("AV123", departure, arrival, 1L, 1L, 2L, List.of(1L, 2L));
        var resp = new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(1L, 2L), List.of());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/flights/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("AV123"));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var resp = new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of());

        when(service.findById(1L)).thenReturn(resp);

        mvc.perform(get("/api/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("AV123"))
                .andExpect(jsonPath("$.airlineName").value("Avianca"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("Flight 99 not found"));

        mvc.perform(get("/api/flights/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Flight 99 not found"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of()),
                new FlightResponse(2L, "LA456", departure, arrival, 2L, "LATAM", 1L, "BOG", 3L, "CTG", List.of(), List.of())
        );

        when(service.findAll()).thenReturn(flights);

        mvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].number").value("AV123"))
                .andExpect(jsonPath("$[1].number").value("LA456"));
    }

    @Test
    void findByAirline_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of())
        );
        var page = new PageImpl<>(flights);

        when(service.findByAirlineName(eq("Avianca"), any())).thenReturn(page);

        mvc.perform(get("/api/flights/airline/Avianca")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].airlineName").value("Avianca"));
    }

    @Test
    void findByRoute_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of())
        );
        var page = new PageImpl<>(flights);

        when(service.findByRouteAndDate(eq("BOG"), eq("MDE"), any(), any())).thenReturn(page);

        mvc.perform(get("/api/flights/route")
                        .param("origin", "BOG")
                        .param("destination", "MDE")
                        .param("from", departure.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].originCode").value("BOG"))
                .andExpect(jsonPath("$.content[0].destinationCode").value("MDE"));
    }

    @Test
    void search_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of())
        );

        when(service.searchFlights(eq(1L), eq(2L), any(), any())).thenReturn(flights);

        mvc.perform(get("/api/flights/search")
                        .param("originId", "1")
                        .param("destinationId", "2")
                        .param("from", departure.toString())
                        .param("to", arrival.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].number").value("AV123"));
    }

    @Test
    void search_shouldWorkWithOptionalParameters() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, departure.plusHours(2), 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(), List.of())
        );

        when(service.searchFlights(isNull(), isNull(), any(), isNull())).thenReturn(flights);

        mvc.perform(get("/api/flights/search")
                        .param("from", departure.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void findByTags_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(1L, 2L), List.of())
        );

        when(service.findFlightsWithAllTags(anyCollection(), eq(2))).thenReturn(flights);

        mvc.perform(get("/api/flights/tags")
                        .param("tags", "WiFi", "Breakfast")
                        .param("required", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].tagIds.size()").value(2));
    }

    @Test
    void findByTags_shouldUseDefaultRequiredValue() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var flights = List.of(
                new FlightResponse(1L, "AV123", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(1L), List.of())
        );

        when(service.findFlightsWithAllTags(anyCollection(), eq(1))).thenReturn(flights);

        mvc.perform(get("/api/flights/tags")
                        .param("tags", "WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
        
        verify(service).findFlightsWithAllTags(anyCollection(), eq(1));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now().plusDays(1);
        var arrival = departure.plusHours(2);
        var req = new FlightUpdateRequest("AV123-UPD", departure, arrival, 1L, 1L, 2L, List.of(1L));
        var resp = new FlightResponse(1L, "AV123-UPD", departure, arrival, 1L, "Avianca", 1L, "BOG", 2L, "MDE", List.of(1L), List.of());

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/api/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("AV123-UPD"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/flights/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}