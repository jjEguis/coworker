package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@Validated
public class PassengerController {
    private final PassengerService service;

    @PostMapping
    public ResponseEntity<PassengerResponse> create(@Valid @RequestBody PassengerCreateRequest req,
                                                                  UriComponentsBuilder uri){
        var body = service.create(req);
        var location = uri.path("/api/passengers/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }


    @GetMapping
    public ResponseEntity<List<PassengerResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponse> update(@PathVariable Long id,
                                                              @Valid @RequestBody PassengerUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @GetMapping("/{email}")
    public ResponseEntity<PassengerResponse> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @GetMapping("/{email}")
    public ResponseEntity<PassengerResponse> getByEmailAndPassengerProfile(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmailAndPassengerProfile(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
