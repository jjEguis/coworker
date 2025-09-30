package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;

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
        return new AirportDtos.AirportResponse(
                a.getId(),
                a.getCode(),
                a.getName(),
                a.getCity()
        );
    }
}

