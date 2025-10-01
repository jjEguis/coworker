package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface AirlineMapper {

    @Mapping(target = "code")
    Airline toEntity(AirlineCreateRequest req);

    @Mapping(target = "code")
    AirlineResponse toResponse(Airline airline);

    // Actualiza una entidad existente con datos del UpdateRequest
    @Mapping(target = "id", ignore = true)
    void updateEntity(AirlineUpdateRequest dto, @MappingTarget Airline entity);



}
