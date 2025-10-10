package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirlineRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirportRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.TagRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public FlightResponse create(FlightCreateRequest request) {
        // Validar y cargar entidades relacionadas
        Airline airline = airlineRepository.findById(request.airlineId())
                .orElseThrow(() -> new NotFoundException("Airline not found with id: " + request.airlineId()));

        Airport origin = airportRepository.findById(request.originId())
                .orElseThrow(() -> new NotFoundException("Origin airport not found with id: " + request.originId()));

        Airport destination = airportRepository.findById(request.destinationId())
                .orElseThrow(() -> new NotFoundException("Destination airport not found with id: " + request.destinationId()));

        // Crear entidad Flight
        Flight flight = FlightMapper.toEntity(request);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);

        // Manejar tags si se proporcionan
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.tagIds());
            flight.getTags().addAll(tags);
        }

        Flight savedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(savedFlight);
    }

    @Override
    @Transactional
    public FlightResponse update(Long id, FlightUpdateRequest request) {
        // Buscar vuelo existente
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));

        // Validar y cargar entidades relacionadas
        Airline airline = airlineRepository.findById(request.airlineId())
                .orElseThrow(() -> new NotFoundException("Airline not found with id: " + request.airlineId()));

        Airport origin = airportRepository.findById(request.originId())
                .orElseThrow(() -> new NotFoundException("Origin airport not found with id: " + request.originId()));

        Airport destination = airportRepository.findById(request.destinationId())
                .orElseThrow(() -> new NotFoundException("Destination airport not found with id: " + request.destinationId()));

        // Actualizar campos
        FlightMapper.updateEntity(flight, request);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);

        // Actualizar tags si se proporcionan
        if (request.tagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(request.tagIds());
            flight.getTags().clear();
            flight.getTags().addAll(tags);
        }

        Flight updatedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(updatedFlight);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));
        flightRepository.delete(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse findById(Long id) {
        return flightRepository.findById(id)
                .map(FlightMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> findAll() {
        return flightRepository.findAll().stream()
                .map(FlightMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> findByAirlineName(String name, Pageable pageable) {
        return flightRepository.findByAirlineName(name, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> findByRouteAndDate(String origin, String destination,
                                                   OffsetDateTime from, Pageable pageable) {
        // Calcular fecha "hasta" (24 horas después)
        OffsetDateTime to = from.plusDays(1);

        return flightRepository.findFlightByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                        origin, destination, from, to, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> searchFlights(Long originId, Long destinationId,
                                              OffsetDateTime from, OffsetDateTime to) {
        // Obtener entidades Airport si se proporcionan IDs
        Airport origin = originId != null ?
                airportRepository.findById(originId)
                        .orElseThrow(() -> new NotFoundException("Origin airport not found")) :
                null;

        Airport destination = destinationId != null ?
                airportRepository.findById(destinationId)
                        .orElseThrow(() -> new NotFoundException("Destination airport not found")) :
                null;

        // Si no se proporciona "to", establecer un rango por defecto
        OffsetDateTime endDate = to != null ? to : from.plusDays(1);

        return flightRepository.searchFlight(origin, destination, from, endDate).stream()
                .map(FlightMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> findFlightsWithAllTags(Collection<String> tags, int required) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        return flightRepository.findFlightsWithAllTags(tags, required).stream()
                .map(FlightMapper::toResponse)
                .toList();
    }
}
