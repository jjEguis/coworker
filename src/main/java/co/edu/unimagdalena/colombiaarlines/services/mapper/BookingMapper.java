package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface BookingMapper {

    Booking toEntity(BookingCreateRequest req);

    @Mapping(target = "id",ignore = true)
    void updateEntity(BookingUpdateRequest req, @MappingTarget Booking entity);

    BookingResponse toResponse(Booking booking);
}
