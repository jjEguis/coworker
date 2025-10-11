package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos.SeatInventoryUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeatInventoryService {

    SeatInventoryResponse create(SeatInventoryCreateRequest req);

    SeatInventoryResponse get(Long id);
    
    List<SeatInventoryResponse> list();

    SeatInventoryResponse update(Long id, SeatInventoryUpdateRequest req);

    void delete(Long id);

    SeatInventoryResponse findByFlightAndCabin(Long flightId, String cabin);
}