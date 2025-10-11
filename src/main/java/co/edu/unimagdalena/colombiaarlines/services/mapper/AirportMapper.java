package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightsOrigin", ignore = true)
    @Mapping(target = "flightsDestination", ignore = true)
    Airport toEntity(AirportCreateRequest req);


    AirportResponse toResponse(Airport airport);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true) // Se asume que no se actualiza
    @Mapping(target = "flightsOrigin", ignore = true)
    @Mapping(target = "flightsDestination", ignore = true)
    void updateEntity(AirportUpdateRequest req, @MappingTarget Airport entity);

}

