package co.edu.unimagdalena.colombiaarlines.services.mapper;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.*;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingItemDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking toEntity(BookingCreateRequest req, Passenger passenger, List<BookingItem> items) {
        return Booking.builder()
                .createdAt(req.createdAt())
                .passenger(passenger)
                .items(items)
                .build();
    }

    public static void updateEntity(Booking booking, BookingUpdateRequest req, List<BookingItem> items) {
        booking.setItems(items);
    }

    public static BookingResponse toResponse(Booking booking) {
        var items = booking.getItems() == null
                ? List.<BookingItemDtos.BookingItemResponse>of()
                : booking.getItems().stream()
                .map(BookingItemMapper::toResponse)
                .collect(Collectors.toList());

        return new BookingResponse(
                booking.getId(),
                booking.getCreatedAt(),
                booking.getPassenger() == null ? null : PassengerMapper.toResponse(booking.getPassenger()),
                items
        );
    }
}
