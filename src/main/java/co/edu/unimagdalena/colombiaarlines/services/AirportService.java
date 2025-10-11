package co.edu.unimagdalena.colombiaarlines.services;

import java.util.List;

public interface AirportService {

    AirportResponse create(AirportCreateRequest req);
    AirportResponse getById(Long id);
    AirportResponse getByCode(String code);
    List<AirportResponse> list();
    void update(Long id, AirportUpdateRequest req);
    void delete(Long id);
}
