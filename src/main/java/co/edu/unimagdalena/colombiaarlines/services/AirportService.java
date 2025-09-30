package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;

import java.util.List;

public interface AirportService {

    AirportResponse create(AirportCreateRequest req);
    AirportResponse getById(Long id);
    AirportResponse getByCode(String code);
    List<AirportResponse> list();
    void delete(Long id);
}
