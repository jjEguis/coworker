package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
@Validated
public class AirportController {

    private final AirportService service;

    @PostMapping
    public ResponseEntity<AirportResponse> create(@Valid @RequestBody AirportCreateRequest req,
                                                  UriComponentsBuilder uri) {
        var body = service.create(req);
        var location = uri.path("/api/airports/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<AirportResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}