package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;

import java.util.List;

public interface AirlineService {
    AirlineResponse create(AirlineCreateRequest req);
    AirlineResponse get(Long id);
    AirlineResponse getByCode(String code);
    List<AirlineResponse> list();
    void delete(Long id);
}
