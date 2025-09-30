package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.SeatInventory;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.SeatInventoryRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository inventoryRepo;
    private final FlightRepository flightRepo; // Necesario para la relación

    @Override
    public SeatInventoryResponse create(SeatInventoryCreateRequest req) {
        // Lógica de negocio: Validar que los asientos disponibles no excedan el total.
        if (req.availableSeats() > req.totalSeats()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats.");
        }
        
        // Buscar Flight para la relación
        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(req.flightId())));

        SeatInventory inventory = SeatInventoryMapper.toEntity(req, flight);

        return SeatInventoryMapper.toResponse(inventoryRepo.save(inventory));
    }

    @Override 
    @Transactional(readOnly = true)
    public SeatInventoryResponse get(Long id) {
        return inventoryRepo.findById(id)
                .map(SeatInventoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Seat Inventory %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInventoryResponse> list() {
        return inventoryRepo.findAll().stream()
                .map(SeatInventoryMapper::toResponse)
                .toList();
    }

    @Override
    public SeatInventoryResponse update(Long id, SeatInventoryUpdateRequest req) {
        SeatInventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat Inventory %d not found".formatted(id)));
                
        // Lógica de negocio: Revalidar la restricción al actualizar
        if (req.availableSeats() > req.totalSeats()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats.");
        }

        // Mapeo de actualización (no necesitamos buscar Flight de nuevo)
        SeatInventoryMapper.updateEntity(inventory, req);

        return SeatInventoryMapper.toResponse(inventoryRepo.save(inventory));
    }

    @Override
    public void delete(Long id) {
        if (!inventoryRepo.existsById(id)) {
            throw new NotFoundException("Seat Inventory %d not found".formatted(id));
        }
        inventoryRepo.deleteById(id);
    }




    @Transactional(readOnly = true)
    @Override
    public SeatInventoryResponse findByFlightAndCabin(Long flightId, Cabin cabin) {
        // metodo creado en seatInventoryRepository
        return inventoryRepo.findByFlightIdAndCabin(flightId, cabin)
                .map(SeatInventoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Inventory not found for Flight %d and Cabin %s".formatted(flightId, cabin)));
    }
}