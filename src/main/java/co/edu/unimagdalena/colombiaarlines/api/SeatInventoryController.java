package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.services.SeatInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/seat-inventories")
@RequiredArgsConstructor
@Validated
public class SeatInventoryController {

    private final SeatInventoryService service;

    @PostMapping
    public ResponseEntity<SeatInventoryResponse> create(@Valid @RequestBody SeatInventoryCreateRequest req,
                                                         UriComponentsBuilder uri) {
        var body = service.create(req);
        var location = uri.path("/api/seat-inventories/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/flight/{flightId}/cabin/{cabin}")
    public ResponseEntity<SeatInventoryResponse> findByFlightAndCabin(
            @PathVariable Long flightId,
            @PathVariable Cabin cabin) {
        return service.findByFlightAndCabin(flightId, cabin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/flight/{flightId}/cabin/{cabin}/available")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long flightId,
            @PathVariable Cabin cabin,
            @RequestParam(defaultValue = "1") int min) {
        return ResponseEntity.ok(service.availableSeats(flightId, cabin, min));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SeatInventoryResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody SeatInventoryUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}