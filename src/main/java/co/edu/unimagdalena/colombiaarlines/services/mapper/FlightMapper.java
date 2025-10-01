package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

    public interface FlightMapper {

        Flight toEntity(FlightCreateRequest req);

        @Mapping(target = "id", ignore = true)
        void updateEntity(Flight flight, @MappingTarget Flight entity);

        FlightResponse toResponse(Flight f);
    }