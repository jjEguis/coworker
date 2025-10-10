package co.edu.unimagdalena.colombiaarlines.DTOs;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set; // O List, dependiendo de tu mapeo de colección
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemCreateRequest; // Para crear items anidados
import co.edu.unimagdalena.colombiaarlines.DTOs.BookingItemDtos.BookingItemResponse; // Para respuesta de items anidados

public class BookingDtos {
    public record BookingCreateRequest(Long passengerId, OffsetDateTime createdAt, List<BookingItemResponse> items) implements Serializable {}
    public record BookingUpdateRequest(List<BookingItemCreateRequest> items) implements Serializable {}

    public record BookingResponse(
            Long id,
            OffsetDateTime createdAt,
            PassengerResponse passenger,
            List<BookingItemResponse> items
    ) implements Serializable {}
}