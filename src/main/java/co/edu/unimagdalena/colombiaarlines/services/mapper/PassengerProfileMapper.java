package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PassengerProfileMapper {

    PassengerProfile toEntity(PassengerProfileDto entity);

    PassengerProfileDto toResponse(PassengerProfile entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passeger", ignore = true)
    void updateEntity(PassengerProfileDto dto, @MappingTarget PassengerProfile profile);
}
