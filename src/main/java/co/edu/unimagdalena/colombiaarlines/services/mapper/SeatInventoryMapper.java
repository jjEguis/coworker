package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatInventoryMapper {
    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "flight", ignore = true)
    SeatInventory toEntity(SeatInventoryCreateRequest req, Flight flight);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "cabin",  ignore = true)
    @Mapping(target = "flight",  ignore = true)
    void updateEntity(SeatInventoryUpdateRequest req, @MappingTarget SeatInventory entity);

    @Mapping(target = "flightId", source = "flight.id")
    SeatInventoryResponse toResponse(SeatInventory si);
}

