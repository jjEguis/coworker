package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.domine.entities.*;

public class FlightMapper {

    public static Flight toEntity(FlightCreateRequest request) {
        return Flight.builder()
                .number(request.number())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .build();
    }

    public static void updateEntity(Flight flight, FlightUpdateRequest request) {
        if (request.number() != null) {
            flight.setNumber(request.number());
        }
        if (request.departureTime() != null) {
            flight.setDepartureTime(request.departureTime());
        }
        if (request.arrivalTime() != null) {
            flight.setArrivalTime(request.arrivalTime());
        }
    }

    public static FlightResponse toResponse(Flight flight) {
        if (flight == null) return null;

        return new FlightResponse(
                flight.getId(),
                flight.getNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAirline() != null ? flight.getAirline().getId() : null,
                flight.getAirline() != null ? flight.getAirline().getName() : null,
                flight.getOrigin() != null ? flight.getOrigin().getId() : null,
                flight.getOrigin() != null ? flight.getOrigin().getCode() : null,
                flight.getDestination() != null ? flight.getDestination().getId() : null,
                flight.getDestination() != null ? flight.getDestination().getCode() : null,
                flight.getTags().stream().map(Tag::getId).toList(),
                flight.getSeatInventories().stream().map(SeatInventory::getId).toList()
        );
    }
}