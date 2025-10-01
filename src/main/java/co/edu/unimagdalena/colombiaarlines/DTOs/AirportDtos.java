package co.edu.unimagdalena.colombiaarlines.DTOs;

import java.io.Serializable;

public class AirportDtos {

    public record AirportCreateRequest(String code, String name, String city) implements Serializable {}
    public record AirportUpdateRequest(String name, String city) implements Serializable {}
    public record AirportResponse(Long id, String code, String name, String city) implements Serializable {}
}