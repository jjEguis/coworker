package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirportDtos.*;

import java.util.List;

public interface AirportService {

    AirportResponse create(AirportCreateRequest req);
    AirportResponse get(Long id);
    AirportResponse getByCode(String code);
    List<AirportResponse> list();
    AirportResponse update(Long id, AirportUpdateRequest req);
    void delete(Long id);
    // ADD methods add and remove
}
