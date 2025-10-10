package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirlineRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.AirlineMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository repo;

    @Override public AirlineResponse create(AirlineCreateRequest req){
        return AirlineMapper.toResponse(repo.save(AirlineMapper.toEntity(req)));
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse get(Long id){
        return repo.findById(id).map(AirlineMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airline not found"));
    }


    @Override
    @Transactional(readOnly = true)
    public AirlineResponse getByCode(String code){
        return repo.findByCode(code).map(AirlineMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airline not found by code"));
    }

    @Override @Transactional(readOnly = true)
    public List<AirlineResponse> list() {
        return repo.findAll().stream().map(AirlineMapper::toResponse).toList();
    }

    @Override
    public AirlineResponse update(Long id, AirlineUpdateRequest req) {

        var airline = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline not found with id: " + id));

        AirlineMapper.patch(airline, req);

        var updated = repo.save(airline);
        return AirlineMapper.toResponse(updated);
    }


    @Override
    public void delete(Long id){
        repo.deleteById(id);
    }

}
