package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;

public class BookingItemMapper {

    //  Crear entidad desde CreateRequest
    public static BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest dto,
                                       Booking booking,
                                       Flight flight) {
        BookingItem item = new BookingItem();
        item.setCabin(dto.cabin());
        item.setPrice(dto.price());
        item.setSegmentOrder(dto.segmentOrder());
        item.setBooking(booking);
        item.setFlight(flight);
        return item;
    }

    //  Actualizar entidad existente desde UpdateRequest
    public static void updateEntity(BookingItem item,
                                    BookingItemDtos.BookingItemUpdateRequest dto,
                                    Flight flight) {
        if (dto.cabin() != null) item.setCabin(dto.cabin());
        if (dto.price() != null) item.setPrice(dto.price());
        if (dto.segmentOrder() != null) item.setSegmentOrder(dto.segmentOrder());
        if (flight != null) item.setFlight(flight);
    }

    //  Convertir entidad a Response
    public static BookingItemDtos.BookingItemResponse toResponse(BookingItem item) {

        return new BookingItemDtos.BookingItemResponse(
                item.getId(),
                item.getCabin(),
                item.getPrice(),
                item.getSegmentOrder(),
                item.getBooking() != null ? item.getBooking().getId() : null,
                item.getFlight() != null ? item.getFlight().getId() : null
        );
    }
}
