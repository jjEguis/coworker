package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirlineRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.AirlineMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository repo;
    private final AirlineMapper airlineMapper;

    @Override public AirlineResponse create(AirlineCreateRequest req){
        return airlineMapper.toResponse(repo.save(airlineMapper.toEntity(req)));  // Este servicio no crea los flights, por lo tanto tampoco los actualiza
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse get(Long id){
        return repo.findById(id).map(airlineMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airline not found"));
    }


    @Override
    @Transactional(readOnly = true)
    public AirlineResponse getByCode(String code){
        return repo.findByCode(code).map(airlineMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airline not found by code"));
    }

    @Override @Transactional(readOnly = true)
    public List<AirlineResponse> list() {
        return repo.findAll().stream().map(airlineMapper::toResponse).toList();
    }

    @Override
    public AirlineResponse update(Long id, AirlineUpdateRequest req) {
        Airline airline = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(id)));

        // Usamos el metodo de instancia para la actualización / Inyeccion
        airlineMapper.updateEntity(req, airline);

        return airlineMapper.toResponse(repo.save(airline));
    }

    @Override
    public void delete(Long id){
        Airline airline = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(id)));

        if (!airline.getFlights().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete airline with associated flights. Delete flights first."
            );      // Manejo de relacion del lado Flight
        }

        repo.deleteById(id);
    }
}
