package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos;
import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirportRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.AirlineMapper;
import co.edu.unimagdalena.colombiaarlines.services.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AirportServiceImpl implements AirportService {

    private final AirportRepository repo;

    @Override
    public AirportResponse create(AirportCreateRequest req) {
        return AirportMapper.toResponse(repo.save(AirportMapper.toEntity(req)));
    }

    @Override @Transactional(readOnly = true)
    public AirportResponse getById(Long id) {
        return repo.findById(id).map(AirportMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airport not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResponse getByCode(String code){
        return repo.findByCode(code).map(AirportMapper::toResponse)
                .orElseThrow(()->new NotFoundException("Airline not found by code"));
    }

    @Override @Transactional(readOnly = true)
    public List<AirportResponse> list() {
        return repo.findAll().stream().map(AirportMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id){
        repo.deleteById(id);
    }

}
