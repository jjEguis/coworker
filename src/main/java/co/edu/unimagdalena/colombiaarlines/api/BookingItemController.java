package co.edu.unimagdalena.colombiaarlines.api;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.services.BookingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/{bookingId}/items")
@RequiredArgsConstructor
@Validated
public class BookingItemController {

    private final BookingItemService service;

    @PostMapping
    public ResponseEntity<BookingItemResponse> add(@PathVariable Long bookingId,
                                                       @Valid @RequestBody BookingItemCreateRequest req,
                                                       UriComponentsBuilder uriBuilder) {
        var body = service.addItem(bookingId, req);
        var location = uriBuilder
                .path("/api/bookings/{bookingId}/items/{itemId}")
                .buildAndExpand(bookingId, body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public ResponseEntity<List<BookingItemResponse>> getByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(service.findByBookingIdSegmentOrder(bookingId));
    }

    @GetMapping("{id}")
    public BigDecimal getTotalPrice(@PathVariable Long id){
       return service.getTotalPrice(id);
    }

    @GetMapping("/{flightId}/{cabin}")
    public Long seatsSold(@PathVariable Long flightId,@PathVariable Cabin cabin){
        return service.seatsSold(flightId, cabin);
    }

    @PutMapping
    public ResponseEntity<BookingItemResponse> updateBookingItem(@RequestBody BookingItemUpdateRequest req){
        return ResponseEntity.ok(service.updateBookingItem(req));
    }

    @GetMapping
    public ResponseEntity<List<BookingItemResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookingItem(@PathVariable Long id){
       service.delete(id);
       return ResponseEntity.noContent().build();
    }
}
