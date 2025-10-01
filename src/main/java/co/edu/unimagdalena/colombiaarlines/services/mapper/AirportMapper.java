package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface AirportMapper {

    Airport toEntity(AirportCreateRequest req);

    AirportResponse toResponse(Airport airport);
    @Mapping(target = "id",ignore = true)
    void updateEntity(AirportResponse req, @MappingTarget Airport entity);


}

