package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightService {
    FlightResponse create(FlightCreateRequest request);
    FlightResponse update(Long id, FlightUpdateRequest request);
    void delete(Long id);
    FlightResponse findById(Long id);
    List<FlightResponse> findAll();
    Page<FlightResponse> findByAirlineName(String name, Pageable pageable);
    Page<FlightResponse> findByRouteAndDate(String origin, String destination,
                                            OffsetDateTime from, Pageable pageable);
    List<FlightResponse> searchFlights(Long originId, Long destinationId,
                                       OffsetDateTime from, OffsetDateTime to);
    List<FlightResponse> findFlightsWithAllTags(Collection<String> tags, int required);
}
