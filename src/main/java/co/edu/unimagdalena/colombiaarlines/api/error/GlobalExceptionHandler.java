package co.edu.unimagdalena.colombiaarlines.api.error;

import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para todos los controllers.
 * Convierte excepciones en respuestas HTTP estandarizadas usando RFC 7807 (Problem Details).
 * Esto evita tener try-catch en cada controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja NotFoundException (404 NOT FOUND)
     * 
     * Ejemplo: GET /api/airlines/999 cuando no existe
     * Response:
     * {
     *   "type": "about:blank",
     *   "title": "Not Found",
     *   "status": 404,
     *   "detail": "Airline 999 not found",
     *   "instance": "/api/airlines/999",
     *   "timestamp": "2025-10-10T15:30:00Z"
     * }
     */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Maneja errores de validación (400 BAD REQUEST)
     * 
     * Ejemplo: POST /api/airlines sin 'code' requerido
     * Response:
     * {
     *   "type": "about:blank",
     *   "title": "Validation Failed",
     *   "status": 400,
     *   "detail": "Invalid request body",
     *   "instance": "/api/airlines",
     *   "errors": {
     *     "code": "must not be null",
     *     "name": "must not be blank"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request body");
        problem.setTitle("Validation Failed");
        problem.setProperty("timestamp", Instant.now());
        
        // Extraer todos los errores de validación
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        problem.setProperty("errors", errors);
        
        return problem;
    }

    /**
     * Maneja IllegalStateException (409 CONFLICT)
     * 
     * Ejemplo: DELETE /api/airlines/1 cuando tiene vuelos asociados
     * Response:
     * {
     *   "type": "about:blank",
     *   "title": "Conflict",
     *   "status": 409,
     *   "detail": "Cannot delete airline with associated flights. Delete flights first.",
     *   "instance": "/api/airlines/1"
     * }
     */
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Business Rule Violation");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Maneja IllegalArgumentException (400 BAD REQUEST)
     * 
     * Ejemplo: POST /api/seat-inventories con availableSeats > totalSeats
     * Response:
     * {
     *   "type": "about:blank",
     *   "title": "Bad Request",
     *   "status": 400,
     *   "detail": "Available seats cannot exceed total seats."
     * }
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid Argument");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    /**
     * Maneja cualquier excepción no capturada (500 INTERNAL SERVER ERROR)
     * 
     * Este es el "catch-all" para errores inesperados.
     * IMPORTANTE: En producción, NO expongas el stack trace al cliente.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "An unexpected error occurred"
        );
        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        
        // Solo en desarrollo (comentar en producción)
        problem.setProperty("exception", ex.getClass().getSimpleName());
        // problem.setProperty("message", ex.getMessage());  // Filtro de info sensible
        
        return problem;
    }
}