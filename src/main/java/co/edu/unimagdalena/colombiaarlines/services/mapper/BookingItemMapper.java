package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface BookingItemMapper {

    BookingItem toEntity(BookingItemCreateRequest req);

    @Mapping(target = "id",ignore = true)
    void updateEntity(BookingItemUpdateRequest req, @MappingTarget BookingItem entity);

    BookingItemResponse toResponse(BookingItem b);
}
