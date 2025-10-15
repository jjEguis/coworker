package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.services.BookingItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingItemController.class)
class BookingItemControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean BookingItemService service;

    @Test
    void add_shouldReturn201AndLocation() throws Exception {
        var req = new BookingItemCreateRequest(Cabin.ECONOMY, new BigDecimal("150.00"), 1, 1L, 5L);
        var resp = new BookingItemResponse(10L, Cabin.ECONOMY, new BigDecimal("150.00"), 1, 1L, 5L);

        when(service.addItem(eq(1L), any())).thenReturn(resp);

        mvc.perform(post("/api/bookings/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/bookings/1/items/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"));
    }

    @Test
    void listByBooking_shouldReturn200() throws Exception {
        var items = List.of(
                new BookingItemResponse(1L, Cabin.ECONOMY, new BigDecimal("150.00"), 1, 1L, 5L),
                new BookingItemResponse(2L, Cabin.BUSINESS, new BigDecimal("350.00"), 2, 1L, 6L)
        );
        when(service.findByBookingIdSegmentOrder(1L)).thenReturn(items);

        mvc.perform(get("/api/bookings/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$[1].cabin").value("BUSINESS"));
    }

    @Test
    void getTotalPrice_shouldReturn200() throws Exception {
        when(service.getTotalPrice(1L)).thenReturn(new BigDecimal("500.00"));

        mvc.perform(get("/api/bookings/1/items/1/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    void getSeatsSold_shouldReturn200() throws Exception {
        when(service.seatsSold(5L, Cabin.ECONOMY)).thenReturn(42L);

        mvc.perform(get("/api/bookings/1/items/5/seats-sold/ECONOMY"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new BookingItemUpdateRequest(Cabin.BUSINESS, new BigDecimal("400.00"), 1, 5L);
        var resp = new BookingItemResponse(1L, Cabin.BUSINESS, new BigDecimal("400.00"), 1, 1L, 5L);

        when(service.updateBookingItem(any())).thenReturn(resp);

        mvc.perform(put("/api/bookings/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabin").value("BUSINESS"))
                .andExpect(jsonPath("$.price").value(400.00));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/bookings/1/items/5"))
                .andExpect(status().isNoContent());
        verify(service).delete(5L);
    }
}