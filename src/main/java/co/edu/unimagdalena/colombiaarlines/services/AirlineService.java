package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.AirlineDtos.*;

import java.util.List;

public interface AirlineService {
    AirlineResponse create(AirlineCreateRequest req);
    AirlineResponse get(Long id);
    AirlineResponse getByCode(String code);
    List<AirlineResponse> list();
    AirlineResponse update(Long id, AirlineUpdateRequest Req);
    void delete(Long id);
}
