package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "seatInventories", ignore = true)
    Flight toEntity(FlightCreateRequest req, Airline airline, Airport origin, Airport destination, List<Tag> tags);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tagsId", ignore = true)
    @Mapping(target = "seatInventories", ignore = true)
    FlightResponse toResponse(Flight f);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    //@Mapping(target = "departureTime", ignore = true)
    //@Mapping(target = "arrivalTime", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "seatInventories", ignore = true)
    void updateEntity(@MappingTarget Flight flight, FlightUpdateRequest req, Airline airline, Airport origin, Airport destination, List<Tag> tags);
}