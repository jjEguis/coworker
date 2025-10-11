package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.AirportDtos;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.FlightDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;

import java.util.List;

public class AirportMapper {

    public static Airport toEntity(AirportDtos.AirportCreateRequest req) {
        return Airport.builder()
                .code(req.code())
                .name(req.name())
                .city(req.city())
                .build();
    }

    public static void updateEntity(Airport airport, AirportDtos.AirportUpdateRequest req) {
        airport.setName(req.name());
        airport.setCity(req.city());
    }

    public static AirportDtos.AirportResponse toResponse(Airport a) {
        List<FlightDtos.FlightResponse> flightsOrigin = a.getFlightsOrigin() == null ? List.of() :
                a.getFlightsOrigin().stream().map(FlightMapper::toResponse).toList();
        List<FlightDtos.FlightResponse> flightsDestination = a.getFlightsDestination() == null ? List.of() :
                a.getFlightsDestination().stream().map(FlightMapper::toResponse).toList();
        return new AirportDtos.AirportResponse(
                a.getId(),
                a.getCode(),
                a.getName(),
                a.getCity(),
                flightsOrigin,
                flightsDestination
        );
    }
}

