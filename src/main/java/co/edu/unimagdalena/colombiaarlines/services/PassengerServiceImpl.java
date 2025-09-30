package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // Transacción por defecto para métodos de escritura (create, update, delete)
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    // NOTA: El PassengerMapper NO se inyecta, se usa de forma estática.

    // ------------------------------------
    // Operación CREATE
    // ------------------------------------
    @Override
    public PassengerResponse create(PassengerCreateRequest req) {
        // 1. Convertir DTO -> Entidad (Llamada Estática)
        // El Mapper crea la entidad Passenger y el PassengerProfile anidado.
        Passenger passenger = PassengerMapper.toEntity(req);

        // 2. Lógica de Negocio: Establecer la relación bidireccional (si PassengerProfile lo requiere)
        PassengerProfile profile = passenger.getPassengerProfile();
        if (profile != null) {
            // Esto es crucial si el perfil no se guarda en cascada o tiene FK bidireccional
            profile.setPassenger(passenger);
        }

        // 3. Persistir y Mapear a Respuesta (Llamada Estática)
        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    // ------------------------------------
    // Operación GET (Lectura)
    // ------------------------------------
    @Override
    @Transactional(readOnly = true) // Optimiza la consulta
    public PassengerResponse get(Long id) {
        return passengerRepository.findById(id)
                // Usando '::' (method reference) para mapear. ¡Muy limpio!
                .map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));
    }

    // ------------------------------------
    // Operación LIST (Lectura)
    // ------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponse> list() {
        return passengerRepository.findAll().stream()
                .map(PassengerMapper::toResponse)
                .toList();
    }

    // Operación UPDATE
    @Override // Método UPDATE de PassengerServiceImpl
    public PassengerResponse update(Long id, PassengerUpdateRequest req) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));

        // 1. Actualiza campos de Passenger (fullName, email)
        PassengerMapper.updateEntity(passenger, req);

        // 2. Actualiza campos de PassengerProfile (teléfono, código de país)
        PassengerProfile profile = passenger.getPassengerProfile();
        if (profile != null && req.profile() != null) {
            PassengerMapper.updateProfileEntity(profile, req.profile());
        }

        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    // ------------------------------------
    // Operación DELETE
    // ------------------------------------
    @Override
    public void delete(Long id) {
        // Lógica de negocio opcional: Verificar si existe antes de borrar
        if (!passengerRepository.existsById(id)) {
            throw new NotFoundException("Passenger %d not found".formatted(id));
        }
        passengerRepository.deleteById(id);
    }
}