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
    public ResponseEntity<List<BookingItemResponse>> listByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(service.findByBookingIdSegmentOrder(bookingId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<BookingItemResponse> update(@PathVariable Long bookingId,
                                                       @PathVariable Long itemId,
                                                       @Valid @RequestBody BookingItemUpdateRequest req) {
        return ResponseEntity.ok(service.updateBookingItem(req));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long bookingId,
                                        @PathVariable Long itemId) {
        service.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}