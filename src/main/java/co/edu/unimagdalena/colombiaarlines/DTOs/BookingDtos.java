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

    // Para Response, con DTOs de relaciones completas
    public record BookingResponse(
            Long id,
            OffsetDateTime createdAt,
            PassengerResponse passenger, // O solo Long passengerId si no necesitas el detalle
            Set<BookingItemResponse> items // O List, según tu mapeo
    ) implements Serializable {}
}