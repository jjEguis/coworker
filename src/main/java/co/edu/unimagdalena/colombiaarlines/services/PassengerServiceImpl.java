package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    public PassengerResponse create(PassengerCreateRequest req) {
        var passenger = PassengerMapper.toEntity(req);
        var saved = passengerRepository.save(passenger);
        return PassengerMapper.toResponse(saved);
    }

    @Override
    public PassengerResponse get(Long id) {
        var passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pasajero no encontrado con id: " + id));
        return PassengerMapper.toResponse(passenger);
    }

    @Override @Transactional(readOnly = true)
    public List<PassengerResponse> list() {
        return passengerRepository.findAll().stream().map(PassengerMapper::toResponse).toList();
    }

    @Override
    public PassengerResponse update(Long id, PassengerUpdateRequest req) {
        var passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pasajero no encontrado con id: " + id));

        PassengerMapper.updateEntity(passenger,req);
        passengerRepository.save(passenger);

        return PassengerMapper.toResponse(passenger);
    }

    @Override @Transactional(readOnly = true)
    public PassengerResponse getByEmail(String email) {
        return passengerRepository.findByEmailIgnoreCase(email).map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Member email %s not found".formatted(email)));
    }

    @Override @Transactional(readOnly = true)
    public PassengerResponse getByEmailAndPassengerProfile(String email) {
        return passengerRepository.findByEmailIgnoreCaseAndPassengerProfile(email).map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Member email %s not found".formatted(email)));
    }

    @Override
    public void delete(Long id) {
        var passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pasajero no encontrado con id: " + id));
        passengerRepository.delete(passenger);
    }
}