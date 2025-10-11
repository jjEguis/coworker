package co.edu.unimagdalena.colombiaarlines.DTOs;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set; // O List, dependiendo de tu mapeo de colección
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemCreateRequest; // Para crear items anidados
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemResponse; // Para respuesta de items anidados

public class BookingDtos {

    public record BookingCreateRequest(Long passengerId, OffsetDateTime createdAt, Set<BookingItemCreateRequest> items) implements Serializable {}
    public record BookingUpdateRequest(Set<BookingItemCreateRequest> items) implements Serializable {} // createdAt y passengerId usualmente no se actualizan
    public record BookingResponse(
            Long id,
            OffsetDateTime createdAt,
            PassengerResponse passenger,
            Set<BookingItemResponse> items
    ) implements Serializable {}
}