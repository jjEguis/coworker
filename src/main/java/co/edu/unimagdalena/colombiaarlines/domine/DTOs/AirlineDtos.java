package co.edu.unimagdalena.colombiaarlines.domine.DTOs;

import java.io.Serializable;

public class AirlineDtos {
    public record AirlineCreateRequest(String code, String name) implements Serializable {}
    public record AirlineUpdateRequest(String name) implements Serializable {} // Generalmente el 'code' no se actualiza
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
}