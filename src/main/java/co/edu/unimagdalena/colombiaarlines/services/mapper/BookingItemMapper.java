package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;

public class BookingItemMapper {

    public static BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest req){
        return BookingItem.builder().cabin(req.cabin()).price(req.price()).segmentOrder(req.segmentOrder()).build();
    }

    public static void updateEntity(BookingItem bookingItem, BookingItemDtos.BookingItemUpdateRequest req, Flight flight) {
        bookingItem.setFlight(flight); // depende si permites actualizar el vuelo o no
        bookingItem.setCabin(req.cabin());
        bookingItem.setPrice(req.price());
        bookingItem.setSegmentOrder(req.segmentOrder());
    }

    public static BookingItemDtos.BookingItemResponse toResponse(BookingItem b) {
        return new BookingItemDtos.BookingItemResponse(
                b.getId(),
                b.getFlight() == null ? null : FlightMapper.toResponse(b.getFlight()),
                b.getCabin(),
                b.getPrice(),
                b.getSegmentOrder()
        );
    }
}
