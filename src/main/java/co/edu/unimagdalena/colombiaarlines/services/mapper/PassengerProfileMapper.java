package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerProfileDto;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {PassengerProfileDto.class})
public interface PassengerProfileMapper {
    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "passenger",  ignore = true)
    PassengerProfile toEntity(PassengerProfileDto entity);

    PassengerProfileDto toResponse(PassengerProfile entity);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "passenger",  ignore = true)
    void updateEntity(PassengerProfileDto dto, @MappingTarget PassengerProfile profile);
}
