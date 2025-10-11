package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Validated
public class FlightController {

    private final FlightService service;

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest req,
                                                  UriComponentsBuilder uri) {
        var body = service.create(req);
        var location = uri.path("/api/flights/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/airline/{name}")
    public ResponseEntity<Page<FlightResponse>> findByAirline(@PathVariable String name,
                                                               Pageable pageable) {
        return ResponseEntity.ok(service.findByAirlineName(name, pageable));
    }

    @GetMapping("/route")
    public ResponseEntity<Page<FlightResponse>> findByRoute(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByRouteAndDate(origin, destination, from, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> search(
            @RequestParam(required = false) Long originId,
            @RequestParam(required = false) Long destinationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(service.searchFlights(originId, destinationId, from, to));
    }

    @GetMapping("/tags")
    public ResponseEntity<List<FlightResponse>> findByTags(
            @RequestParam Collection<String> tags,
            @RequestParam(defaultValue = "1") int required) {
        return ResponseEntity.ok(service.findFlightsWithAllTags(tags, required));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody FlightUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}