package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.SeatInventoryDtos.SeatInventoryCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.SeatInventoryDtos.SeatInventoryResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.SeatInventoryDtos.SeatInventoryUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.SeatInventoryRepository;
import co.edu.unimagdalena.colombiaarlines.services.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightRepository flightRepository;

    @Override
    public SeatInventoryResponse create(SeatInventoryCreateRequest req) {
        Flight flight = flightRepository.findById(req.flightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        var seatInventory = SeatInventoryMapper.toEntity(req,flight);
        var saved = seatInventoryRepository.save(seatInventory);
        return SeatInventoryMapper.toResponse(saved);
    }

    @Override
    public List<SeatInventoryResponse> list() {
        return seatInventoryRepository.findAll().stream()
                .map(SeatInventoryMapper::toResponse)
                .toList();
    }

    @Override
    public SeatInventoryResponse update(Long id, SeatInventoryUpdateRequest req) {
        var seatInventory = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario de asientos no encontrado con id: " + id));

        SeatInventoryMapper.updateEntity(seatInventory, req);
        seatInventoryRepository.save(seatInventory);
        return SeatInventoryMapper.toResponse(seatInventory);
    }

    @Override
    public void delete(Long id) {
        var seatInventory = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        seatInventoryRepository.delete(seatInventory);
    }

    @Override
    public boolean availableSeats(Long flightId, Cabin cabin, int min) {
        return seatInventoryRepository.availableSeats(flightId, cabin, min);
    }

    @Override
    public Optional<SeatInventoryResponse> findByFlightAndCabin(Long flightId, Cabin cabin) {
        return seatInventoryRepository.findSeatInventoriesByFlight_IdAndCabin(flightId, cabin)
                .map(SeatInventoryMapper::toResponse);
    }
}