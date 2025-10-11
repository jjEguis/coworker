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
import co.edu.unimagdalena.colombiaarlines.services.mapper.SeatInventoryMapper; // Interfaz MapStruct
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository inventoryRepo;
    private final FlightRepository flightRepo;
    private final SeatInventoryMapper inventoryMapper; // Inyección de MapStruct

    @Override
    public SeatInventoryResponse create(SeatInventoryCreateRequest req) {
        // Validar que los asientos disponibles no excedan el total.
        if (req.availableSeats() > req.totalSeats()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats.");
        }
        // Buscamos la relacion
        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(req.flightId())));

        // Mappear  campos no relacionados
        SeatInventory inventory = inventoryMapper.toEntity(req, flight);

        // Gestionar ambos lados de la relacion
        flight.addSeatInventory(inventory); // Se encarga de todo
        //inventory.setFlight(flight); // Unnecessary


        // Guardar y Retornar
        return inventoryMapper.toResponse(inventoryRepo.save(inventory));
    }

    @Override
    @Transactional(readOnly = true)
    public SeatInventoryResponse get(Long id) {
        return inventoryRepo.findById(id)
                .map(inventoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Seat Inventory %d not found".formatted(id)));
    }

    // pa las reservas de asientos
    @Override
    @Transactional(readOnly = true)
    public SeatInventoryResponse findByFlightAndCabin(Long flightId, String cabin) {
        return inventoryRepo.findByFlightIdAndCabin(flightId, Cabin.valueOf(cabin))
                .map(inventoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Inventory not found for Flight %d and Cabin %s".formatted(flightId, cabin)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInventoryResponse> list() {
        return inventoryRepo.findAll().stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    @Override
    public SeatInventoryResponse update(Long id, SeatInventoryUpdateRequest req) {
        SeatInventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat Inventory %d not found".formatted(id)));

        if (req.availableSeats() > req.totalSeats()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats.");
        }

        inventoryMapper.updateEntity(req, inventory);

        return inventoryMapper.toResponse(inventoryRepo.save(inventory));
    }

    @Override
    public void delete(Long id) {
        SeatInventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat Inventory %d not found".formatted(id)));

        inventory.getFlight().removeSeatInventory(inventory); // Helper method; gestiona ambos lados de la relacion

        inventoryRepo.deleteById(id);
    }

}