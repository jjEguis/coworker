package co.edu.unimagdalena.colombiaarlines.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AirlineDtos {

    public record AirlineCreateRequest(String code, String name) implements Serializable {}
    public record AirlineUpdateRequest(String name) implements Serializable {} // Generalmente el 'code' no se actualiza
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
}