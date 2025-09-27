package co.edu.unimagdalena.colombiaarlines.domine.DTOs;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set; // O List, dependiendo de tu mapeo de colección
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.BookingItemDtos.BookingItemCreateRequest; // Para crear items anidados
import co.edu.unimagdalena.colombiaarlines.domine.DTOs.BookingItemDtos.BookingItemResponse; // Para respuesta de items anidados

public class BookingDtos {
    // Asumo un enum BookingStatus en tu dominio
    public enum BookingStatus { PENDING, CONFIRMED, CANCELLED, COMPLETED }

    // Para Create, puedes necesitar el ID del pasajero y una lista de items a crear
    public record BookingCreateRequest(Long passengerId, OffsetDateTime createdAt, BookingStatus status, Set<BookingItemCreateRequest> items) implements Serializable {}
    public record BookingUpdateRequest(BookingStatus status, Set<BookingItemCreateRequest> items) implements Serializable {} // createdAt y passengerId usualmente no se actualizan

    // Para Response, con DTOs de relaciones completas
    public record BookingResponse(
            Long id,
            OffsetDateTime createdAt,
            BookingStatus status,
            PassengerResponse passenger, // O solo Long passengerId si no necesitas el detalle
            Set<BookingItemResponse> items // O List, según tu mapeo
    ) implements Serializable {}
}