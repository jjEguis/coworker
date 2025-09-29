package co.edu.unimagdalena.colombiaarlines.domine.DTOs;

import java.io.Serializable;
import java.math.BigDecimal;
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.FlightDtos.FlightResponse; // Para el DTO anidado

public class BookingItemDtos {
    // Para Create, puedes necesitar el ID del vuelo
    public record BookingItemCreateRequest(Long flightId, BigDecimal price, Integer segmentOrder) implements Serializable {}
    public record BookingItemUpdateRequest(BigDecimal price, Integer segmentOrder) implements Serializable {} // ID del vuelo probablemente no se actualiza
    public record BookingItemResponse(Long id, FlightResponse flight, BigDecimal price, Integer segmentOrder) implements Serializable {}
}