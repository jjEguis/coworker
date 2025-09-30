package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;

import java.util.List;

public class AirlineMapper {

    public static Airline toEntity(AirlineDtos.AirlineCreateRequest req){
        return Airline.builder().code(req.code()).name(req.name()).build();
    }

    public static AirlineResponse toResponse(Airline a){
        var flights = a.getFlights() == null ? List.<FlightResponse>of()
                    : a.getFlights().stream().map(FlightMapper::toResponse).toList();

        return new AirlineResponse(a.getId(),a.getCode(),a.getName());
    }


}
