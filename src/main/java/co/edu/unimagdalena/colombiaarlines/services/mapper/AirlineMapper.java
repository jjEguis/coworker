package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Airline toEntity(AirlineCreateRequest req);

    AirlineResponse toResponse(Airline airline); // Deberia agregar Set<Flight> flights aqui?

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "flights", ignore = true)
    void updateEntity(AirlineUpdateRequest dto, @MappingTarget Airline entity); // Airline no es dueño de la relacion con Flight
}
