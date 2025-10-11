package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.*;
import co.edu.unimagdalena.colombiaarlines.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService service;

    @PostMapping
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagCreateRequest req,
                                               UriComponentsBuilder uri) {
        var body = service.create(req);
        var location = uri.path("/api/tags/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{name}")
    public ResponseEntity<TagResponse> getByName(@PathVariable String name) {
        return service.findTagByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TagResponse>> findByNames(@RequestParam Collection<String> names) {
        return ResponseEntity.ok(service.findTagByNameIn(names));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody TagUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}