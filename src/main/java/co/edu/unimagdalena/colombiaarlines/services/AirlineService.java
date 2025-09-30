package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.AirlineDtos.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AirlineService {
    AirlineResponse create(AirlineCreateRequest req);
    AirlineResponse get(Long id);
    AirlineResponse getByCode(String code);
    List<AirlineResponse> list();
    void delete(Long id);
}
