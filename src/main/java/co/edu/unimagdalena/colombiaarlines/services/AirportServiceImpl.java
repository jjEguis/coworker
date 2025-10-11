package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.AirportCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.AirportResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.AirportUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirportRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.AirportMapper; // Interfaz MapStruct
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository repo;
    private final AirportMapper airportMapper; // Inyección de MapStruct

    @Override
    public AirportResponse create(AirportCreateRequest req) {
        return airportMapper.toResponse(repo.save(airportMapper.toEntity(req)));
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResponse get(Long id) { // Método 'get'
        return repo.findById(id).map(airportMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResponse getByCode(String code){
        return repo.findByCode(code).map(airportMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airport not found by code"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportResponse> list() {
        return repo.findAll().stream().map(airportMapper::toResponse).toList();
    }

    @Override // Método UPDATE
    public AirportResponse update(Long id, AirportUpdateRequest req) {
        Airport airport = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(id)));

        airportMapper.updateEntity(req, airport);

        return airportMapper.toResponse(repo.save(airport));
    }

    @Override
    public void delete(Long id) {
        Airport airport = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(id)));
        if (!airport.getFlightsDestination().isEmpty() && !airport.getFlightsOrigin().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete airport with associated flights. Delete flightsOrigin and flightsDestination first."
            );
        }
        repo.deleteById(id);
    }
}