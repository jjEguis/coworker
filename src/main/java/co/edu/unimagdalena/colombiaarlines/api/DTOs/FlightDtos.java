package co.edu.unimagdalena.colombiaarlines.api.DTOs;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;


public class FlightDtos {

    public record FlightCreateRequest(
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            Long originId,
            Long destinationId,
            List<Long> tagIds
    ) implements Serializable {}

    public record FlightUpdateRequest(
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            Long originId,
            Long destinationId,
            List<Long> tagIds
    ) implements Serializable {}

    public record FlightResponse(
            Long id,
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            String airlineName,
            Long originId,
            String originCode,
            Long destinationId,
            String destinationCode,
            List<Long> tagIds,
            List<Long> seatInventoryIds
    ) implements Serializable {}
}