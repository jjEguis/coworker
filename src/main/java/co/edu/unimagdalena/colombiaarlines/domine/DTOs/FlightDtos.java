package co.edu.unimagdalena.colombiaarlines.domine.DTOs;

import java.io.Serializable;
import java.time.OffsetDateTime;
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.AirlineDtos.AirlineResponse; // Para el DTO anidado
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.AirportDtos.AirportResponse; // Para el DTO anidado

public class FlightDtos {
    // Para Create/Update, podrías necesitar solo los IDs de las entidades relacionadas
    public record FlightCreateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime, Long airlineId, Long originAirportId, Long destinationAirportId) implements Serializable {}
    public record FlightUpdateRequest(OffsetDateTime departureTime, OffsetDateTime arrivalTime, Long airlineId, Long originAirportId, Long destinationAirportId) implements Serializable {}

    // Para Response, puedes incluir los DTOs completos de las relaciones o solo sus IDs/nombres
    public record FlightResponse(
            Long id,
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            AirlineResponse airline, // O solo Long airlineId; si no necesitas el detalle completo
            AirportResponse origin,
            AirportResponse destination
    ) implements Serializable {}
}