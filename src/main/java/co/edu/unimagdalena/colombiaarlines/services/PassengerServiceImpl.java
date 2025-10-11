package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerMapper; // Interfaz MapStruct
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerProfileMapper; // Interfaz MapStruct de la entidad anidada
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper; // Inyección
    private final PassengerProfileMapper profileMapper; // Inyección del perfil

    @Override
    public PassengerResponse create(PassengerCreateRequest req) {
        Passenger passenger = passengerMapper.toEntity(req);

        // Lógica de negocio: establecer la relación bidireccional si es necesario
        PassengerProfile profile = passenger.getPassengerProfile();
        /* if (profile != null) {
            String phone = profile.getPhone();
            String code = profile.getCountryCode();
            if (phone == null || code == null) {
              //  Feature Lanzar excepcion Bad Request
            }*/
        profile.setPassenger(passenger);

        return passengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse get(Long id) {
        return passengerRepository.findById(id)
                .map(passengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponse> list() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::toResponse)
                .toList();
    }

    @Override
    public PassengerResponse update(Long id, PassengerUpdateRequest req) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));

        // 1. Actualizar campos directos
        passengerMapper.updateEntity(req, passenger);

        // 2. Actualizar campos anidados (usando el Mapper del perfil)
        PassengerProfile profile = passenger.getPassengerProfile();
        if (profile != null && req.profile() != null) {
            profileMapper.updateEntity(req.profile(), profile);
        }

        return passengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override
    public void delete(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new NotFoundException("Passenger %d not found".formatted(id));
        }

        passengerRepository.deleteById(id);
    }
}