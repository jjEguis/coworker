package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.PassengerService;
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

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean PassengerService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        var req = new PassengerCreateRequest("Juan Pérez", "juan@example.com", profile);
        var resp = new PassengerResponse(1L, "Juan Pérez", "juan@example.com", profile);

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/passengers/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez"));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        when(service.get(1L)).thenReturn(new PassengerResponse(1L, "Juan Pérez", "juan@example.com", profile));

        mvc.perform(get("/api/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L)).thenThrow(new NotFoundException("Passenger 99 not found"));

        mvc.perform(get("/api/passengers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Passenger 99 not found"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        var passengers = List.of(
                new PassengerResponse(1L, "Juan Pérez", "juan@example.com", profile),
                new PassengerResponse(2L, "María López", "maria@example.com", profile)
        );
        when(service.list()).thenReturn(passengers);

        mvc.perform(get("/api/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getByEmail_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        when(service.getByEmail("juan@example.com"))
                .thenReturn(new PassengerResponse(1L, "Juan Pérez", "juan@example.com", profile));

        mvc.perform(get("/api/passengers/email/juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez"));
    }

    @Test
    void getByEmailWithProfile_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        when(service.getByEmailAndPassengerProfile("juan@example.com"))
                .thenReturn(new PassengerResponse(1L, "Juan Pérez", "juan@example.com", profile));

        mvc.perform(get("/api/passengers/email/juan@example.com/with-profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"))
                .andExpect(jsonPath("$.profile.phone").value("+57"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("+57", "Colombia");
        var req = new PassengerUpdateRequest("Juan Pérez Updated", "juan@example.com", profile);
        var resp = new PassengerResponse(1L, "Juan Pérez Updated", "juan@example.com", profile);

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Juan Pérez Updated"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/passengers/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}