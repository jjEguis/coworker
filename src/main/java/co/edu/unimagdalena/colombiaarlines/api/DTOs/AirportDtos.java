package co.edu.unimagdalena.colombiaarlines.api.DTOs;

import java.io.Serializable;
import java.util.List;

public class AirportDtos {
    public record AirportCreateRequest(String code, String name, String city) implements Serializable {}
    public record AirportUpdateRequest(String name, String city) implements Serializable {}
    public record AirportResponse(Long id, String code,
                                  String name,
                                  String city,
                                  List<FlightDtos.FlightResponse> flightsOrigin,
                                  List<FlightDtos.FlightResponse> flightsDestination) implements Serializable {}
}