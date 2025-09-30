package co.edu.unimagdalena.colombiaarlines.DTOs;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;

import java.io.Serializable;

public class SeatInventoryDtos {

    public record SeatInventoryCreateRequest(Cabin cabin, Integer totalSeats, Integer availableSeats, Long flightId ) implements Serializable {}
    public record SeatInventoryUpdateRequest(Integer totalSeats, Integer availableSeats) implements Serializable {}
    public record SeatInventoryResponse(Long id, Cabin cabin, Integer totalSeats, Integer availableSeats, Long flightId) implements Serializable {}
}
