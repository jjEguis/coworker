package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface SeatInventoryMapper {

    SeatInventory toEntity(SeatInventoryCreateRequest req);

    void updateEntity(SeatInventoryUpdateRequest req, @MappingTarget SeatInventory entity);

    SeatInventoryResponse toResponse(SeatInventory si);
}

