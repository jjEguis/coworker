package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.TagDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.TagService;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
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

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean TagService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new TagCreateRequest("WiFi");
        var resp = new TagResponse(1L, "WiFi");

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/tags/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("WiFi"));
    }

    @Test
    void getByName_shouldReturn200() throws Exception {
        when(service.findTagByName("WiFi")).thenReturn(Optional.of(new TagResponse(1L, "WiFi")));

        mvc.perform(get("/api/tags/WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("WiFi"));
    }

    @Test
    void getByName_shouldReturn404WhenNotFound() throws Exception {
        when(service.findTagByName("NonExistent")).thenReturn(Optional.empty());

        mvc.perform(get("/api/tags/NonExistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByNames_shouldReturn200() throws Exception {
        var tags = List.of(
                new TagResponse(1L, "WiFi"),
                new TagResponse(2L, "Breakfast")
        );
        when(service.findTagByNameIn(anyCollection())).thenReturn(tags);

        mvc.perform(get("/api/tags/search")
                        .param("names", "WiFi", "Breakfast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("WiFi"))
                .andExpect(jsonPath("$[1].name").value("Breakfast"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var tags = List.of(
                new TagResponse(1L, "WiFi"),
                new TagResponse(2L, "Breakfast")
        );
        when(service.list()).thenReturn(tags);

        mvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new TagUpdateRequest("WiFi-Updated");
        var resp = new TagResponse(1L, "WiFi-Updated");

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("WiFi-Updated"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/tags/1"))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }
}