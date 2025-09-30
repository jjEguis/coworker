package co.edu.unimagdalena.colombiaarlines.DTOs;

import java.io.Serializable;
import java.math.BigDecimal;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightResponse; // Para el DTO anidado
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;

public class BookingItemDtos {
    // Para Create, puedes necesitar el ID del vuelo
    public record BookingItemCreateRequest(Long flightId, Cabin cabin, BigDecimal price, Integer segmentOrder) implements Serializable {}
    public record BookingItemUpdateRequest(Cabin cabin, BigDecimal price, Integer segmentOrder) implements Serializable {} // ID del vuelo probablemente no se actualiza
    public record BookingItemResponse(Long id, FlightResponse flight, Cabin cabin, BigDecimal price, Integer segmentOrder) implements Serializable {}
}