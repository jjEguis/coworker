package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", ignore = true)
    BookingItem toEntity(BookingItemCreateRequest req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void updateEntity(BookingItemUpdateRequest req, @MappingTarget BookingItem entity);

    @Mapping(target = "flight", ignore = true)
    BookingItemResponse toResponse(BookingItem b);
}
