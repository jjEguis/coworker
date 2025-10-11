package co.edu.unimagdalena.colombiaarlines.api.DTOs;

import java.io.Serializable;
//import co.edu.unimagdalena.colombiaarlines.domine.DTOs.PassengerProfileDtos.PassengerProfileDto; // Importar el DTO anidado

public class PassengerDtos {

    public record PassengerCreateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable {}
    public record PassengerUpdateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable {}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable {} // Usado dentro de PassengerDtos
    public record PassengerResponse(Long id, String fullName, String email, PassengerProfileDto profile) implements Serializable {}
}