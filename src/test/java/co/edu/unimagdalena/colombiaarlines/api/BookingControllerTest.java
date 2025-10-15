package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.*;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.services.BookingService;
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

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean BookingService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var now = OffsetDateTime.now();
        var req = new BookingCreateRequest(1L, now, List.of());
        var passenger = new PassengerResponse(1L, "Juan Pérez", "juan@example.com", null);
        var resp = new BookingResponse(10L, now, passenger, List.of());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/bookings/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var now = OffsetDateTime.now();
        var passenger = new PassengerResponse(1L, "Juan Pérez", "juan@example.com", null);
        var resp = new BookingResponse(1L, now, passenger, List.of());

        when(service.searchBooking(1L)).thenReturn(resp);

        mvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.passenger.fullName").value("Juan Pérez"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.searchBooking(99L)).thenThrow(new NotFoundException("Booking 99 not found"));

        mvc.perform(get("/api/bookings/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Booking 99 not found"));
    }

    @Test
    void findByPassengerEmail_shouldReturn200() throws Exception {
        var now = OffsetDateTime.now();
        var passenger = new PassengerResponse(1L, "Juan Pérez", "juan@example.com", null);
        var bookings = List.of(
                new BookingResponse(1L, now, passenger, List.of()),
                new BookingResponse(2L, now, passenger, List.of())
        );
        var page = new PageImpl<>(bookings);

        when(service.findByPassenger_Email(eq("juan@example.com"), any())).thenReturn(page);

        mvc.perform(get("/api/bookings/passenger/juan@example.com")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].passenger.email").value("juan@example.com"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var now = OffsetDateTime.now();
        var req = new BookingUpdateRequest(List.of());
        var passenger = new PassengerResponse(1L, "Juan Pérez", "juan@example.com", null);
        var resp = new BookingResponse(1L, now, passenger, List.of());

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}