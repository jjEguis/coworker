package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightUpdateRequest;

import java.util.List;

public interface FlightService {

    FlightResponse create(FlightCreateRequest req);

    FlightResponse get(Long id);

    List<FlightResponse> list();

    FlightResponse update(Long id, FlightUpdateRequest req);

    void delete(Long id);

    // Métodos para gestionar la relación N:M con Tags
    FlightResponse addTag(Long flightId, Long tagId);
    FlightResponse removeTag(Long flightId, Long tagId);
}