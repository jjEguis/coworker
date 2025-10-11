package co.edu.unimagdalena.colombiaarlines.api.DTOs;

import java.io.Serializable;
import java.util.List;

public class AirlineDtos {
    public record AirlineCreateRequest(String code, String name) implements Serializable {}
    public record AirlineUpdateRequest(String name, String code) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name, List<FlightDtos.FlightResponse> flights) implements Serializable {}
}