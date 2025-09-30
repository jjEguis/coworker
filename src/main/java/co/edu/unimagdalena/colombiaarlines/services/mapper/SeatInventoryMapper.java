package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;

public class SeatInventoryMapper {

    public static SeatInventory toEntity(SeatInventoryDtos.SeatInventoryCreateRequest req, Flight flight) {
        return SeatInventory.builder()
                .cabin(req.cabin())
                .totalSeats(req.totalSeats())
                .availableSeats(req.availableSeats())
                .flight(flight)
                .build();
    }

    public static void updateEntity(SeatInventory seatInventory, SeatInventoryDtos.SeatInventoryUpdateRequest req) {
        seatInventory.setTotalSeats(req.totalSeats());
        seatInventory.setAvailableSeats(req.availableSeats());
    }

    public static SeatInventoryDtos.SeatInventoryResponse toResponse(SeatInventory si) {
        return new SeatInventoryDtos.SeatInventoryResponse(
                si.getId(),
                si.getCabin(),
                si.getTotalSeats(),
                si.getAvailableSeats(),
                si.getFlight() != null ? si.getFlight().getId() : null
        );
    }
}

