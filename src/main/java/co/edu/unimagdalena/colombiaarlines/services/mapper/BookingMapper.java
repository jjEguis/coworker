package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking toEntity(BookingDtos.BookingCreateRequest req, Passenger passenger, List<BookingItem> items) {
        return Booking.builder()
                .createdAt(req.createdAt())
                .passenger(passenger)
                .items(items)
                .build();
    }

    public static void updateEntity(Booking booking, BookingDtos.BookingUpdateRequest req, List<BookingItem> items) {
        // Normalmente no se cambia passenger ni createdAt
        booking.setItems(items);
    }

    public static BookingDtos.BookingResponse toResponse(Booking booking) {
        var items = booking.getItems() == null
                ? Set.<BookingItemDtos.BookingItemResponse>of()
                : booking.getItems().stream()
                .map(BookingItemMapper::toResponse)
                .collect(Collectors.toSet());

        return new BookingDtos.BookingResponse(
                booking.getId(),
                booking.getCreatedAt(),
                booking.getPassenger() == null ? null : PassengerMapper.toResponse(booking.getPassenger()),
                items
        );
    }
}
