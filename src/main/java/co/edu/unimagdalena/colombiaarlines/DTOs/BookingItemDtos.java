package co.edu.unimagdalena.colombiaarlines.DTOs;

import java.io.Serializable;
import java.math.BigDecimal;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightResponse; // Para el DTO anidado
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;

public class BookingItemDtos {
    public record BookingItemCreateRequest(Cabin cabin, BigDecimal price, Integer segmentOrder, Long bookingId, Long flightId) implements Serializable {}
    public record BookingItemUpdateRequest(Cabin cabin, BigDecimal price, Integer segmentOrder, Long flightId) implements Serializable {}
    public record BookingItemResponse(Long id, Cabin cabin, BigDecimal price, Integer segmentOrder,Long bookingId, Long flightId) implements Serializable {}
}